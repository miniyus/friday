package com.miniyus.friday.infrastructure.auth.login.filter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.util.StreamUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.auth.login.PasswordAuthentication;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Slf4j
public class PasswordAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    private static final String DEFAULT_LOGIN_REQUEST_URL = "/login"; // "/login"으로 오는 요청을 처리
    private static final String HTTP_METHOD = "POST"; // 로그인 HTTP 메소드는 POST
    private static final String CONTENT_TYPE = "application/json"; // JSON 타입의 데이터로 오는 로그인 요청만 처리
    private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
            new AntPathRequestMatcher(
                    DEFAULT_LOGIN_REQUEST_URL, HTTP_METHOD); // "/login" + POST로 온 요청에 매칭된다.

    private final ObjectMapper objectMapper;

    public PasswordAuthenticationFilter(
            AntPathRequestMatcher loginPathRequestMatcher,
            ObjectMapper objectMapper) {
        super(loginPathRequestMatcher);
        this.objectMapper = objectMapper;
    }

    public PasswordAuthenticationFilter(ObjectMapper objectMapper) {
        super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER); // 위에서 설정한 "login" + POST로 온 요청을 처리하기 위해 설정
        this.objectMapper = objectMapper;
    }

    /**
     * 인증 처리 메소드
     *
     * UsernamePasswordAuthenticationFilter와 동일하게 UsernamePasswordAuthenticationToken 사용
     * StreamUtils를 통해 request에서 messageBody(JSON) 반환 요청 JSON Example { "email" : "aaa@bbb.com"
     * "password" : "test123" } 꺼낸 messageBody를 objectMapper.readValue()로 Map으로 변환 (Key : JSON의 키 ->
     * email, password) Map의 Key(email, password)로 해당 이메일, 패스워드 추출 후
     * UsernamePasswordAuthenticationToken의 파라미터 principal, credentials에 대입
     *
     * AbstractAuthenticationProcessingFilter(부모)의 getAuthenticationManager()로 AuthenticationManager
     * 객체를 반환 받은 후 authenticate()의 파라미터로 UsernamePasswordAuthenticationToken 객체를 넣고 인증 처리 (여기서
     * AuthenticationManager 객체는 ProviderManager -> SecurityConfig에서 설정)
     */
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request,
            HttpServletResponse response)
            throws AuthenticationException, IOException {
        if (request.getContentType() == null || !request.getContentType().equals(CONTENT_TYPE)) {
            throw new AuthenticationServiceException(
                    "Authentication Content-Type not supported: " + request.getContentType());
        }

        String messageBody =
                StreamUtils.copyToString(request.getInputStream(), StandardCharsets.UTF_8);

        PasswordAuthentication usernamePassword =
                objectMapper.readValue(messageBody, PasswordAuthentication.class);

        String email = usernamePassword.getUsername();
        String password = usernamePassword.getPassword();
        log.debug("input email: {}", email);
        UsernamePasswordAuthenticationToken authRequest =
                new UsernamePasswordAuthenticationToken(email, password);
        return this.getAuthenticationManager().authenticate(authRequest);
    }
}
