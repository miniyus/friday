package com.meteormin.friday.infrastructure.security.annotation;

import io.swagger.v3.oas.annotations.Hidden;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 인증 유저 주입을 위한 annotation
 * @author seongminyoo
 * @since 2023/10/23
 * @see com.meteormin.friday.infrastructure.security.AuthUserMethodResolver
 */
@Target(ElementType.PARAMETER)
@Retention(RetentionPolicy.RUNTIME)
@Hidden
public @interface AuthUser {
    /**
     * true: 인증 유저의 entity 객체를 주입
     * false: principal user info 객체를 주입
     * @return boolean
     */
    boolean entity() default false;
}
