package com.miniyus.friday.infrastructure.auth;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.miniyus.friday.infrastructure.auth.login.PasswordAuthenciationProvider;
import com.miniyus.friday.infrastructure.auth.login.filter.JwtAuthenticationFilter;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2AccessDeniedHandler;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2AuthenticationEntryPoint;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2FailureHandler;
import com.miniyus.friday.infrastructure.auth.oauth2.handler.OAuth2SuccessHandler;

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
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final PasswordAuthenciationProvider passwordAuthenciationProvider;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http.authorizeHttpRequests(auth -> auth
				.requestMatchers(AntPathRequestMatcher.antMatcher("/h2-console/**")).permitAll()
				.requestMatchers(AntPathRequestMatcher.antMatcher("/v1/**")).permitAll()
				.anyRequest().authenticated());
		http.formLogin(form -> form.disable());
		http.oauth2Login(oauth2Login -> oauth2Login
				.loginPage("/oauth2/login")
				.authorizationEndpoint(authorization -> authorization
						.baseUri("/oauth2/authorization"))
				.userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
						.userService(userService))
				.redirectionEndpoint(redirect -> redirect
						.baseUri("/oauth2/callback/**"))
				.successHandler(successHandler)
				.failureHandler(failureHandler));
		http.userDetailsService(userDetailsService);
		http.authenticationProvider(passwordAuthenciationProvider);
		http.addFilterBefore(new JwtAuthenticationFilter(authenticationManager(http)),
				UsernamePasswordAuthenticationFilter.class);
		http.exceptionHandling(exceptHandling -> exceptHandling
				.authenticationEntryPoint(authenticationEntryPoint)
				.accessDeniedHandler(accessDeniedHandler));
		http.headers(headers -> headers.frameOptions(opt -> opt.disable()));
		http.csrf(csrf -> csrf.disable());
		return http.build();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(HttpSecurity http)
			throws Exception {
		AuthenticationManagerBuilder authenticationManagerBuilder = http
				.getSharedObject(AuthenticationManagerBuilder.class);
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
		return authenticationManagerBuilder.build();
	}
}
