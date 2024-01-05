package com.meteormin.friday.infrastructure.security.social.handler;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.infrastructure.security.AuthResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * [description]
 *
 * @author meteormin
 * @since 2023/09/01
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
        var code = RestErrorCode.ACCESS_DENIED;
        responseHandler.handleErrorResponse(
            request,
            response,
            code,
            "error.accessDenied");
    }

}
