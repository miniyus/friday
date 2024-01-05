package com.meteormin.friday.infrastructure.security.auth.handler;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.infrastructure.security.AuthResponseHandler;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import java.io.IOException;

/**
 * JWT 로그인 실패 시 처리하는 핸들러 SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 * @author meteormin
 * @since 2023/09/04
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    /**
     * authResponseHandler
     */
    private final AuthResponseHandler responseHandler;

    /**
     * Handles the authentication failure event.
     *
     * @param request   the HTTP servlet request
     * @param response  the HTTP servlet response
     * @param exception the authentication exception
     * @throws IOException if an I/O error occurs
     */
    @Override
    public void onAuthenticationFailure(
        HttpServletRequest request,
        HttpServletResponse response,
        AuthenticationException exception) throws IOException {

        log.debug("Failure login. message: {}", exception.getMessage());

        var message = exception.getMessage();

        responseHandler.handleErrorResponse(
            request,
            response,
            RestErrorCode.BAD_REQUEST,
            message);
    }
}
