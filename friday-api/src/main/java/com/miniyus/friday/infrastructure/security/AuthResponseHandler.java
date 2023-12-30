package com.miniyus.friday.infrastructure.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.ErrorResponse;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import com.miniyus.friday.infrastructure.security.social.response.OAuth2TokenResponse;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import java.io.IOException;

@RequiredArgsConstructor
@Component
public class AuthResponseHandler {
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;

    public void handleErrorResponse(
        HttpServletResponse response,
        ErrorCode errorCode,
        String message
    ) throws IOException {
        String translateMessage = messageSource
            .getMessage(message, null, message, LocaleContextHolder.getLocale());
        var errorResponse = new ErrorResponse(errorCode, translateMessage);
        handleJsonResponse(response, errorCode.getHttpStatus(), errorResponse);
    }

    public void handleOAuth2IssueTokenResponse(
        HttpServletResponse response,
        PrincipalUserInfo userInfo,
        IssueToken token) throws IOException {

        OAuth2TokenResponse tokenResponse = new OAuth2TokenResponse(
            userInfo.getId(),
            userInfo.getSnsId(),
            userInfo.getProvider().value(),
            userInfo.getEmail(),
            token);
        handleJsonResponse(response, HttpStatus.CREATED, tokenResponse);
    }

    public void handleOAuth2IssueTokenHeader(
        HttpServletResponse response,
        IssueToken token
    ) {
        response.setStatus(HttpStatus.CREATED.value());
        response.setHeader(token.accessTokenKey(), "Bearer " + token.accessToken());
        response.setHeader(token.refreshTokenKey(), "Bearer " + token.refreshToken());
    }

    public void handlePasswordIssueTokenResponse(
        HttpServletResponse response,
        PrincipalUserInfo userInfo,
        IssueToken token) throws IOException {

        PasswordTokenResponse tokenResponse = new PasswordTokenResponse(
            userInfo.getId(),
            userInfo.getUsername(),
            userInfo.getName(),
            token);
        handleJsonResponse(response, HttpStatus.CREATED, tokenResponse);
    }

    private void handleJsonResponse(HttpServletResponse response,
        HttpStatus status,
        Object body) throws IOException {

        response.setHeader("Content-Type", "application/json");
        response.setStatus(status.value());
        String jsonBody = objectMapper.writeValueAsString(body);
        response.getWriter().write(jsonBody);
    }
}
