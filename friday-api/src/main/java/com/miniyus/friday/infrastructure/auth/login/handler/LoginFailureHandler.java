package com.miniyus.friday.infrastructure.auth.login.handler;

import java.io.IOException;
import java.time.LocalDateTime;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.infrastructure.advice.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * JWT 로그인 실패 시 처리하는 핸들러
 * SimpleUrlAuthenticationFailureHandler를 상속받아서 구현
 */
@Slf4j
@RequiredArgsConstructor
public class LoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException {
        response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json;charset=UTF-8");

        String jsonBody = objectMapper.writeValueAsString(new ErrorResponse(
                ErrorCode.BAD_REQUEST,
                exception.getMessage()));

        response.getWriter().write(jsonBody);
        log.debug("로그인에 실패했습니다. 메시지 : {}", exception.getMessage());
    }
}