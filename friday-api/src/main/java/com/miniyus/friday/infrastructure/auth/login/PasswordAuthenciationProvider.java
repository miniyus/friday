package com.miniyus.friday.infrastructure.auth.login;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import com.miniyus.friday.infrastructure.auth.PrincipalUserDetailsService;
import com.miniyus.friday.infrastructure.auth.PrincipalUserInfo;

import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Component
@RequiredArgsConstructor
public class PasswordAuthenciationProvider implements AuthenticationProvider {

    private final PrincipalUserDetailsService userDetailsService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken) authentication;
        String username = token.getName();
        String password = (String) token.getCredentials();
        PrincipalUserInfo userInfo = userDetailsService.passwordAuthentication(username, password);

        return new UsernamePasswordAuthenticationToken(userInfo, password, userInfo.getAuthorities());
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
