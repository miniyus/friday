package com.meteormin.friday.annotation;

import com.meteormin.friday.infrastructure.security.social.SocialProvider;
import com.meteormin.friday.users.domain.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * WithMockCustomUser
 *
 * @author meteormin
 * @date 2023/09/07
 */
@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithMockCustomUserSecurityContextFactory.class)
public @interface WithMockCustomUser {

    /**
     * @return user's id
     */
    long id() default 1L;

    /**
     * @return username
     */
    String username() default "";

    /**
     * @return name
     */
    String name() default "";

    /**
     * @return role
     */
    UserRole role() default UserRole.USER;

    /**
     * @return SocialProvider
     */
    SocialProvider provider() default SocialProvider.NONE;
}
