package com.miniyus.friday.infrastructure.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class AuthAspect extends LoggingAspect{
    public AuthAspect() {
        super("Auth", LoggerFactory.getLogger(AuthAspect.class));
    }

    @Pointcut("within(com.miniyus.friday.auth.*)")
    public void authPoint() {
    }

    @Before("authPoint()")
    public void beforeAuth(JoinPoint joinPoint) {
        beforeLogging("Auth", joinPoint);
    }

    @AfterReturning(pointcut = "authPoint()", returning = "returnValue")
    public void afterAuthRetuning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Auth",joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "authPoint()", throwing = "e")
    public void afterAuthThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Auth", joinPoint, e);
    }
}
