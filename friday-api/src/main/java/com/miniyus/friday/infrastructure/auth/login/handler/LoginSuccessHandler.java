package com.miniyus.friday.infrastructure.auth.login.handler;

import java.io.IOException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.auth.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.auth.login.response.PasswordTokenResponse;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Slf4j
@RequiredArgsConstructor
public class LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.access.expiration}")
    private String accessTokenExpiration;

    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException {
        var userDetails = (PrincipalUserInfo) authentication.getPrincipal();
        IssueToken tokens = jwtService.issueToken(userDetails.getId());

        log.debug("로그인에 성공하였습니다. 이메일 : {}", userDetails.getUsername());
        log.debug("로그인에 성공하였습니다. AccessToken : {}", tokens.getAccessToken());
        log.debug("발급된 AccessToken 만료 기간 : {}", tokens.getExpiresIn());

        PasswordTokenResponse passwordTokenResponse = new PasswordTokenResponse(
                userDetails.getId(),
                userDetails.getUsername(),
                userDetails.getName(),
                tokens);

        String jsonBody = objectMapper.writeValueAsString(passwordTokenResponse);
        response.setHeader("Content-Type", "application/json");
        response.setStatus(HttpStatus.CREATED.value());
        response.getWriter().write(jsonBody);

    }
}