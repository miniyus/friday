package com.miniyus.friday.integration.annotation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import org.springframework.security.test.context.support.WithSecurityContext;

import com.miniyus.friday.infrastructure.auth.UserRole;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/07
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {
    String username() default "username";

    String name() default "name";

    UserRole role() default UserRole.USER;
}
