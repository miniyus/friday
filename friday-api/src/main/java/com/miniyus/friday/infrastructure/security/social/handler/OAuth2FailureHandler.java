package com.miniyus.friday.infrastructure.security.social.handler;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * [description]
 *
 * @author seongminyoo
 * @since 2023/08/31
 */
@Component
@Slf4j
@RequiredArgsConstructor
public class OAuth2FailureHandler implements AuthenticationFailureHandler {
    private final AuthResponseHandler responseHandler;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        log.debug("Failure oauth login: {}", exception.getMessage());

        RestErrorCode code = RestErrorCode.INVALID_CLIENT;
        var message = exception.getMessage();

        responseHandler.handleErrorResponse(
                response,
                code,
                message);

    }
}
