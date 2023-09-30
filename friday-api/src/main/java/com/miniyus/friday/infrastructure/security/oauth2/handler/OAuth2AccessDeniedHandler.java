package com.miniyus.friday.infrastructure.security.oauth2.handler;

import java.io.IOException;

import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
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
@Component
@RequiredArgsConstructor
public class OAuth2AccessDeniedHandler implements AccessDeniedHandler {
    private final AuthResponseHandler responseHandler;

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
        AccessDeniedException exception) throws IOException {
        log.debug("Access Denied: {}", exception.getMessage());
        var code = AuthErrorCode.ACCESS_DENIED;
        responseHandler.handleErrorResponse(
            response,
            code,
            "error.accessDenied");
    }

}
