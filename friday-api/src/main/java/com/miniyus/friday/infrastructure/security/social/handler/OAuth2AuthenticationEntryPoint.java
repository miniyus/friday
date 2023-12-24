package com.miniyus.friday.infrastructure.security.social.handler;

import java.io.IOException;

import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;
import com.miniyus.friday.common.error.AuthErrorCode;
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
@Slf4j
@RequiredArgsConstructor
@Component
public class OAuth2AuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final AuthResponseHandler responseHandler;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
        AuthenticationException exception) throws IOException {
        log.debug("Authentication Exception: {}", exception.getMessage());
        var code = AuthErrorCode.ACCESS_DENIED;
        responseHandler.handleErrorResponse(
            response,
            code,
            "error.accessDenied"
        );
    }

}
