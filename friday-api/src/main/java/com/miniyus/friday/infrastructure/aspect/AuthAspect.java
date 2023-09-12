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
public class AuthAspect extends LoggingAspect {

    // auth api aspect
    public AuthAspect() {
        super("Auth", LoggerFactory.getLogger(AuthAspect.class));
    }

    // signup controller point
    @Pointcut("within(com.miniyus.friday.infrastructure.auth.login.SignupController)")
    public void signupPoint() {}

    @Before("signupPoint()")
    public void beforeSignup(JoinPoint joinPoint) throws Throwable {
        beforeLogging("Signup", joinPoint);
    }

    @AfterReturning(pointcut = "signupPoint()", returning = "returnValue")
    public void afterSignup(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Signup", joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "signupPoint()", throwing = "e")
    public void afterDomainThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging("Signup", joinPoint, e);
    }

    // oauth2 api point
    @Pointcut("within(com.miniyus.friday.infrastructure.auth.oauth2.*)")
    public void oAuth2Point() {}

    @Before("oAuth2Point()")
    public void beforeOAuth2(JoinPoint joinPoint) throws Throwable {
        beforeLogging("OAuth2", joinPoint);
    }

    @AfterReturning(pointcut = "oAuth2Point()", returning = "returnValue")
    public void afterOAuth2(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("OAuth2", joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "oAuth2Point()", throwing = "e")
    public void afterOAuth2Throwing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging("OAuth2", joinPoint, e);
    }

    // services point
    @Pointcut("within(com.miniyus.friday.infrastructure.auth.*)")
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
