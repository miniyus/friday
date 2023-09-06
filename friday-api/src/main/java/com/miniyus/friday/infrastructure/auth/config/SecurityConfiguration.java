package com.miniyus.friday.infrastructure.auth.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.auth.PrincipalUserDetailsService;
import com.miniyus.friday.infrastructure.auth.PrincipalUserService;
import com.miniyus.friday.infrastructure.auth.login.filter.PasswordAuthenticationFilter;
import com.miniyus.friday.infrastructure.auth.login.handler.LoginFailureHandler;
import com.miniyus.friday.infrastructure.auth.login.handler.LoginSuccessHandler;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2AccessDeniedHandler;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2AuthenticationEntryPoint;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2FailureHandler;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2SuccessHandler;
import com.miniyus.friday.infrastructure.jwt.JwtAuthenticationFilter;
import com.miniyus.friday.infrastructure.jwt.JwtService;
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
@RequiredArgsConstructor
public class SecurityConfiguration {
	private final PrincipalUserService userService;
	private final PrincipalUserDetailsService userDetailsService;
	private final OAuth2SuccessHandler successHandler;
	private final OAuth2FailureHandler failureHandler;
	private final OAuth2AuthenticationEntryPoint authenticationEntryPoint;
	private final OAuth2AccessDeniedHandler accessDeniedHandler;
	private final ObjectMapper objectMapper;
	private final JwtService jwtService;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
				// .requestMatchers(AntPathRequestMatcher.antMatcher("/v1/**")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher("/signup")).permitAll()
				.anyRequest().authenticated());
		// .anyRequest().permitAll());
		http.formLogin(form -> form.disable());
		http.httpBasic(basic -> basic.disable());
		http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
		http.csrf(csrf -> csrf.disable());
		http.oauth2Login(oauth2Login -> oauth2Login
				.authorizationEndpoint(authorization -> authorization
						.baseUri("/oauth2/authorization"))
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
						.userService(userService))
				.redirectionEndpoint(redirect -> redirect
						.baseUri("/oauth2/callback/**"))
				.successHandler(successHandler)
				.failureHandler(failureHandler));
		http.userDetailsService(userDetailsService);
		http.exceptionHandling(exceptHandling -> exceptHandling
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler));
		http.headers(headers -> headers.frameOptions(opt -> opt.disable()));

		http.addFilterAfter(passwordAuthenticationFilter(), LogoutFilter.class);
		http.addFilterBefore(jwtAuthenticationFilter(), PasswordAuthenticationFilter.class);
		return http.build();
	}

	@Bean
	public Filter passwordAuthenticationFilter() {
		PasswordAuthenticationFilter loginFilter = new PasswordAuthenticationFilter(
				objectMapper);
		loginFilter.setAuthenticationManager(authenticationManager());
		loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler());
		loginFilter.setAuthenticationFailureHandler(loginFailureHandler());
		return loginFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

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
		return new LoginSuccessHandler(jwtService, objectMapper);
	}

	/**
	 * 로그인 실패 시 호출되는 LoginFailureHandler 빈 등록
	 */
	@Bean
	public LoginFailureHandler loginFailureHandler() {
		return new LoginFailureHandler(objectMapper);
	}

	@Bean
	public JwtAuthenticationFilter jwtAuthenticationFilter() {
		return new JwtAuthenticationFilter(
				jwtService,
				userDetailsService,
				objectMapper);
	}
}
