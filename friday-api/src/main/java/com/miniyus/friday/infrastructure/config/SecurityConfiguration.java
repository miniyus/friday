package com.miniyus.friday.infrastructure.config;

import com.miniyus.friday.infrastructure.security.AuthResponseHandler;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.jwt.JwtAuthenticationFilter;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.security.auth.filter.PasswordAuthenticationFilter;
import com.miniyus.friday.infrastructure.security.auth.handler.LoginFailureHandler;
import com.miniyus.friday.infrastructure.security.auth.handler.LoginSuccessHandler;
import com.miniyus.friday.infrastructure.security.social.handler.OAuth2AccessDeniedHandler;
import com.miniyus.friday.infrastructure.security.social.handler.OAuth2AuthenticationEntryPoint;
import com.miniyus.friday.infrastructure.security.social.handler.OAuth2FailureHandler;
import com.miniyus.friday.infrastructure.security.social.handler.OAuth2SuccessHandler;
import jakarta.servlet.Filter;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security Configuration
 *
 * @author miniyus
 * @date 2023/08/27
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {
    public static final String LOGIN_URL = "/v1/auth/signin";
    public static final String USERINFO_URL = "/v1/auth/me";
    public static final String LOGOUT_URL = "/v1/auth/logout";
    public static final String SIGNUP_URL = "/v1/auth/signup";
    public static final String REFRESH_URL = "/v1/auth/refresh-token";
    public static final String OAUTH2_LOGIN_URL = "/oauth2/authorization";
    public static final String OAUTH2_CALLBACK_URL = "/oauth2/callback";

    private final OAuth2UserService<OAuth2UserRequest, OAuth2User> userService;
    private final UserDetailsService userDetailsService;
    private final MessageSource messageSource;
    private final ObjectMapper objectMapper;
    private final JwtService jwtService;

    /**
     * Generates the function comment for the given function body.
     *
     * @param http the HttpSecurity object
     * @return the SecurityFilterChain object
     * @throws Exception if an error occurs
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 인증 요청 filter
        http.authorizeHttpRequests(auth -> auth
            // 1. h2콘솔 화면 권한 설정
            //   - 로컬 환경에서만 작동하기 때문에 permitAll() 설정도 문제 없다.
            // 2. REST DOC으로 생성된 문서 접근 권한 설정
            // 3. actuator 모니터링 권한 설정
            //   - 로컬 및 개발 환경에서만 작동하기 때문에 permitAll() 설정도 문제 없다.
            // 4. 로그인 요청 권한, 로그인 요청은 인증 없이 요청이 가능해야 하기 때문에
            // 5. 회원 가입
            // 6. 토큰 새로고침 요청
            .requestMatchers(
                AntPathRequestMatcher.antMatcher("/h2-console/**"),
                AntPathRequestMatcher.antMatcher("/docs/**"),
                AntPathRequestMatcher.antMatcher("/actuator/**"),
                AntPathRequestMatcher.antMatcher("/api-docs/**"),
                AntPathRequestMatcher.antMatcher("/v3/api-docs/**"),
                AntPathRequestMatcher.antMatcher(LOGIN_URL),
                AntPathRequestMatcher.antMatcher(SIGNUP_URL),
                AntPathRequestMatcher.antMatcher(REFRESH_URL)
            ).permitAll()
            // 그 외, 모든 요청은 인증이 필요하다.
            .anyRequest().authenticated());

        // spring security의 기본 FormLogin 기능 비활성화
        // REST API로 인증 처리를 진행하기 때문에 불필요하다.
        http.formLogin(AbstractHttpConfigurer::disable);

        // HTTP BasicAuth 비활성화
        http.httpBasic(AbstractHttpConfigurer::disable);
        // session 비활성화
        // jwt 토큰을 활용할 예정이므로 불필요하다.
        http.sessionManagement(
            session -> session.sessionCreationPolicy(
                SessionCreationPolicy.STATELESS));
        // csrf 비활성화
        // JWT 토큰을 이용하여, accessToken, refreshToken을 생성하여 csrf 보안 문제를 어느정도 해결 가능
        http.csrf(AbstractHttpConfigurer::disable);
        // oauth2 로그인 활성화 및 설정
        http.oauth2Login(oauth2Login -> oauth2Login
            // oauth2 로그인 url 설정
            .authorizationEndpoint(authorization -> authorization
                .baseUri(OAUTH2_LOGIN_URL))
            // 회원 정보 서비스 설정
            // oauth2 로그인을 위한 별도의 서비스이다.
            .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                .userService(userService))
            // redirect uri 설정
            .redirectionEndpoint(redirect -> redirect
                .baseUri(OAUTH2_CALLBACK_URL))
            // 인증 성공 핸들러
            .successHandler(authenticationSuccessHandler())
            // 인증 실패 핸들러
            .failureHandler(authenticationFailureHandler()));

        // user detail service 설정
        // 자체 로그인을 위한 servicedlek.
        http.userDetailsService(userDetailsService);

        // 인증 관련 예외 처리 핸들링
        http.exceptionHandling(exceptHandling -> exceptHandling
            // 인증 실패 처리 핸들러
            .authenticationEntryPoint(authenticationEntryPoint())
            // 권한 제한 예외에 대한 핸들러
            .accessDeniedHandler(accessDeniedHandler()));

        http.headers(
            headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable));
        http.addFilterAfter(passwordAuthenticationFilter(), LogoutFilter.class);
        http.addFilterBefore(jwtAuthenticationFilter(), PasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public Filter passwordAuthenticationFilter() {
        AntPathRequestMatcher loginPathRequestMatcher = new AntPathRequestMatcher(
            LOGIN_URL,
            HttpMethod.POST.name());

        PasswordAuthenticationFilter loginFilter = new PasswordAuthenticationFilter(
            loginPathRequestMatcher,
            objectMapper);

        loginFilter.setAuthenticationManager(authenticationManager());
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler());
        return loginFilter;
    }

    public AuthResponseHandler authResponseHandler() {
        return new AuthResponseHandler(
            messageSource,
            objectMapper
        );
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new OAuth2AuthenticationEntryPoint(authResponseHandler());
    }

    @Bean
    public AuthenticationSuccessHandler authenticationSuccessHandler() {
        return new OAuth2SuccessHandler(
            jwtService,
            authResponseHandler()
        );
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return new OAuth2FailureHandler(
            authResponseHandler()
        );
    }

    @Bean
    public AccessDeniedHandler accessDeniedHandler() {
        return new OAuth2AccessDeniedHandler(authResponseHandler());
    }

    /**
     * 암호화 모듈 등록
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 자체 로그인을 위한 인증 매니저 등록
     */
    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailsService);
        return new ProviderManager(provider);
    }

    /**
     * 로그인 성공 시 호출되는 LoginSuccessJWTProviderHandler 빈 등록
     */
    @Bean
    public LoginSuccessHandler loginSuccessHandler() {
        return new LoginSuccessHandler(jwtService, authResponseHandler());
    }

    /**
     * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
     */
    @Bean
    public LoginFailureHandler loginFailureHandler() {
        return new LoginFailureHandler(authResponseHandler());
    }

    /**
     * jwt 인증 및 유효 여부 체크 핸들러
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(
            jwtService,
            userDetailsService,
            objectMapper,
            LOGIN_URL);
    }
}
