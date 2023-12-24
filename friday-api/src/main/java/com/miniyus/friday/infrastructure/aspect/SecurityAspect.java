package com.miniyus.friday.infrastructure.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Security Aspect
 *
 * @author seongminyoo
 * @since 2023/09/11
 */
@Aspect
@Component
public class SecurityAspect extends LoggingAspect {

    /**
     * Auth API aspect
     *
     * @param objectMapper object mapper
     */
    public SecurityAspect(ObjectMapper objectMapper) {
        super(
            "Security",
            LoggerFactory.getLogger(SecurityAspect.class),
            objectMapper);
    }

    // services point
    @Pointcut("within(com.miniyus.friday.infrastructure.security.*)")
    public void securityPoint() {
    }

    @Before("securityPoint()")
    public void beforeSecurity(JoinPoint joinPoint) {
        beforeLogging("Security", joinPoint);
    }

    @AfterReturning(pointcut = "securityPoint()", returning = "returnValue")
    public void afterSecurityReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Security", joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "securityPoint()", throwing = "e")
    public void afterSecurityThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Security", joinPoint, e);
    }
}
