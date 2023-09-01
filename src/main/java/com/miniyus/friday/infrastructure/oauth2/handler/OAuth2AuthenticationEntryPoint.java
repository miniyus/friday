package com.miniyus.friday.infrastructure.oauth2.handler;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.infrastructure.advice.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
        log.debug("Authentication Exception: {}", exception.getMessage());

        AuthErrorCode code = AuthErrorCode.ACCESS_DENINED;
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                code.name(),
                exception.getMessage(),
                null);
        String errorJsonBody = objectMapper.writeValueAsString(errorResponse);

        response.setHeader("Content-Type", "application/json");
        response.setStatus(code.getStatusCode());
        response.getWriter().write(errorJsonBody);
    }

}
