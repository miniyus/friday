package com.meteormin.friday.infrastructure.aspect.annotation;

import org.aspectj.lang.JoinPoint;

import java.lang.annotation.*;

/**
 * 로그인 히스토리를 저장하기 위한 LoginEndPoint 지정
 * @see com.meteormin.friday.infrastructure.aspect.AuthAspect#loginEndPoint
 * @see com.meteormin.friday.infrastructure.aspect.AuthAspect#afterLoginThrowing(JoinPoint, Exception)
 * @see com.meteormin.friday.infrastructure.aspect.AuthAspect#afterLoginRetuning(JoinPoint, Object)
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface LoginEndPoint {
}
