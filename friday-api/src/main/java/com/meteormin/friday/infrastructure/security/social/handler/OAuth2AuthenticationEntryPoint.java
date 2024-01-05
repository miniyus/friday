package com.meteormin.friday.infrastructure.security.social.handler;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.infrastructure.security.AuthResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * OAuth2 Authentication Entry Point
 *
 * @author meteormin
 * @since 2023/09/01
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
        var code = RestErrorCode.ACCESS_DENIED;
        responseHandler.handleErrorResponse(
            request,
            response,
            code,
            "error.accessDenied"
        );
    }

}
