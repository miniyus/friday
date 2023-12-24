package com.miniyus.friday.infrastructure.security.social.handler;

import java.io.IOException;

import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import com.miniyus.friday.common.error.AuthErrorCode;
import org.springframework.security.core.AuthenticationException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
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

        AuthErrorCode code = AuthErrorCode.INVALID_CLIENT;
        var message = exception.getMessage();

        responseHandler.handleErrorResponse(
                response,
                code,
                message);

    }
}
