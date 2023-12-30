package com.miniyus.friday.infrastructure.jwt;

import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Instant;
import java.util.NoSuchElementException;

/**
 * JwtAuthenticationFilter
 *
 * @author miniyus
 * @since 2023/09/04
 */
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private static final String DEFAULT_NO_CHECK_URL = "/login";
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final String loginUrl;

    public JwtAuthenticationFilter(
        JwtService jwtService,
        UserDetailsService userDetailsService,
        String loginUrl) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.loginUrl = loginUrl;
    }

    /**
     * 필터 동작 메서드
     * @param request     request
     * @param response    response
     * @param filterChain filter chain
     */
    @Override
    protected void doFilterInternal(
        @NonNull HttpServletRequest request,
        @NonNull HttpServletResponse response,
        @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        String checkUrl;
        if (this.loginUrl == null || this.loginUrl.isEmpty()) {
            checkUrl = DEFAULT_NO_CHECK_URL;
        } else {
            checkUrl = this.loginUrl;
        }

        if (request.getRequestURI().equals(checkUrl)) {
            filterChain.doFilter(request, response); // "login" 요청이 들어오면, 다음 필터 호출
            return; // return으로 이후 현재 필터 진행 막기 (안해주면 아래로 내려가서 계속 필터 진행시킴)
        }

        // RefreshToken이 없거나 유효하지 않다면, AccessToken을 검사하고 인증을 처리하는 로직 수행
        // AccessToken이 없거나 유효하지 않다면, 인증 객체가 담기지 않은 상태로 다음 필터로 넘어가기 때문에 403 에러 발생
        // AccessToken이 유효하다면, 인증 객체가 담긴 상태로 다음 필터로 넘어가기 때문에 인증 성공

        checkAccessTokenAndAuthentication(request, response, filterChain);

    }

    /**
     * 액세스 토큰 체크 &amp; 인증 처리 메소드
     * <ul>
     * <li>request에서 extractAccessToken()으로 액세스 토큰 추출</li>
     * <li>isTokenValid()로 유효한 토큰인지 검사</li>
     * <li>유효한 토큰이면, 액세스 토큰에서 extractEmail로 Email을 추출
     * <li>findByEmail()로 해당 이메일을 사용하는 유저 객체 반환</li>
     * <li>saveAuthentication()으로 인증 처리하여 인증 허가 </li>
     * <li>처리된 객체를 SecurityContextHolder에 전달</li>
     * <li>다음 인증 필터로 이동</li>
     * </ul>
     */
    public void checkAccessTokenAndAuthentication(HttpServletRequest request,
        HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        log.debug("checkAccessTokenAndAuthentication() 호출");
        log.debug("access token: {}", jwtService.extractAccessToken(request));

        try {
            jwtService.extractAccessToken(request).filter(jwtService::isTokenValid)
                .flatMap(jwtService::getUserByAccessToken).ifPresent(this::saveAuthentication);
        } catch (NoSuchElementException ex) {
            log.debug("error: {}", ex.getMessage());
        }

        log.debug("Next filter");

        filterChain.doFilter(request, response);
    }

    /**
     * 인증 허가 메소드
     * <ul>
     * <li>user: 인증할 user</li>
     * <li>principal: db에 저장된 user</li>
     * <li>{@link UsernamePasswordAuthenticationToken} 객체를 통해 인증 객체인 Authentication 객체 생성</li>
     * <li>Spring SecurityContext에 인증 정보 전달</li>
     * </ul>
     *
     * @param user 인증할 user
     */
    public void saveAuthentication(UserEntity user) {
        log.debug("save auth: {}", user.getEmail());

        var principal =
            (PrincipalUserInfo) userDetailsService.loadUserByUsername(user.getEmail());

        var password = user.getPassword();
        if (principal.isSnsUser() && (password == null || password.trim().isEmpty())) {
            // 소셜 로그인 유저의 비밀번호 임의로 설정 하여 소셜 로그인 유저도 인증 되도록 설정
            long now = Instant.now().getEpochSecond();
            password = String.format("%s-%s", user.getSnsId(), now);
        }

        Authentication authentication = new UsernamePasswordAuthenticationToken(principal, password,
            principal.getAuthorities());

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}
