package com.miniyus.friday.annotation;

import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import net.datafaker.Faker;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.ArrayList;
import java.util.Collection;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/07
 */
public class WithMockCustomUserSecurityContextFactory
    implements WithSecurityContextFactory<WithMockCustomUser> {

    /**
     * Creates a security context based on the given `WithMockCustomUser` annotation.
     *
     * @param annotation the `WithMockCustomUser` annotation containing user details
     * @return the created security context
     */
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser annotation) {
        SecurityContext ctx = SecurityContextHolder.createEmptyContext();

        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(annotation.role().value()));
        String password = new Faker().internet().password();
        var principal = PrincipalUserInfo.builder()
            .id(annotation.id())
            .snsId(null)
            .email(annotation.username())
            .name(annotation.name())
            .password(password)
            .enabled(true)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .credentialsNonExpired(true)
            .attributes(null)
            .provider(annotation.provider())
            .authorities(authorities)
            .role(annotation.role())
            .build();

        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(
                principal,
                "password",
                authorities);

        ctx.setAuthentication(authentication);
        return ctx;
    }
}
