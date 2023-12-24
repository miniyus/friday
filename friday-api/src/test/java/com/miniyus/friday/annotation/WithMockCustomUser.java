package com.miniyus.friday.annotation;

import com.precisionbio.cuttysark.users.domain.Client;
import com.precisionbio.cuttysark.users.domain.UserRole;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * [description]
 *
 * @author miniyus
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
     * @return client
     */
    Client client() default Client.HUMAN;
}
