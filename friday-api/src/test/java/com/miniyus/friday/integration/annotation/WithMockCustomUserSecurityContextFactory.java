package com.miniyus.friday.integration.annotation;

import java.util.ArrayList;
import java.util.Collection;

import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/07
 */
public class WithMockCustomUserSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomUser> {

    /**
     * Creates a security context based on the given `WithMockCustomUser`
     * annotation.
     *
     * @param annotation the `WithMockCustomUser` annotation containing user details
     * @return the created security context
     */
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(annotation.role().getValue()));
        String password = new Faker().internet().password();
        var principal = PrincipalUserInfo.builder()
                .id(1L)
                .snsId(null)
                .username(annotation.username())
                .name(annotation.name())
                .password(password)
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .attributes(null)
                .provider(null)
                .authorities(authorities)
                .role(annotation.role().getValue())
                .build();

        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                principal,
                "password",
                authorities);

        ctx.setAuthentication(authentication);
        return ctx;
    }
}
