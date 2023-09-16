package com.miniyus.friday.infrastructure.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/11
 */
@Aspect
@Component
public class SecurityAspect extends LoggingAspect {

    // security aspect
    public SecurityAspect() {
        super("Security", LoggerFactory.getLogger(SecurityAspect.class));
    }

    // oauth2 api point
    @Pointcut("within(com.miniyus.friday.infrastructure.security.oauth2.*)")
    public void oAuth2Point() {}

    @Before("oAuth2Point()")
    public void beforeOAuth2(JoinPoint joinPoint) throws Throwable {
        beforeLogging("OAuth2", joinPoint);
    }

    @AfterReturning(pointcut = "oAuth2Point()", returning = "returnValue")
    public void afterOAuth2Retuning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("OAuth2", joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "oAuth2Point()", throwing = "e")
    public void afterOAuth2Throwing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging("OAuth2", joinPoint, e);
    }

    // services point
    @Pointcut("within(com.miniyus.friday.infrastructure.security.*)")
    public void applicationPoint() {}

    @Before("applicationPoint()")
    public void beforeApplication(JoinPoint joinPoint) throws Throwable {
        beforeLogging("Application", joinPoint);
    }

    @AfterReturning(pointcut = "applicationPoint()", returning = "returnValue")
    public void afterApplicationReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Application", joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "applicationPoint()", throwing = "e")
    public void afterApplicationThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging("Application", joinPoint, e);
    }
}
