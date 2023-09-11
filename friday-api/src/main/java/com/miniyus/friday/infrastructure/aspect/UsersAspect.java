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
 * @author seongminyoo
 * @date 2023/09/11
 */
@Aspect
@Component
public class UsersAspect extends AbstractAspect {

    public UsersAspect() {
        super("Users", LoggerFactory.getLogger(UsersAspect.class));
    }

    // Adapter layer

    // in/rest
    @Pointcut("within(com.miniyus.friday.users.adapter.in.rest.*)")
    @Override
    public void controllerPoint() {}

    @Before("controllerPoint()")
    @Override
    public void beforeRequest(JoinPoint joinPoint) throws Throwable {
        beforeLogging(REQUEST, joinPoint);
    }

    @AfterReturning(pointcut = "controllerPoint()", returning = "returnValue")
    @Override
    public void afterRequestReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(REQUEST + RETUNING, joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "controllerPoint()", throwing = "e")
    @Override
    public void afterRequestThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(REQUEST + THROWING, joinPoint, e);
    }

    // out/persistence
    @Pointcut("within(com.miniyus.friday.users.adapter.out.persistence.*)")
    @Override
    public void persistencePoint() {}

    @Before("persistencePoint()")
    @Override
    public void beforePersistence(JoinPoint joinPoint) throws Throwable {
        beforeLogging(PERSISTENCE, joinPoint);
    }

    @AfterReturning(pointcut = "persistencePoint()", returning = "returnValue")
    @Override
    public void afterPersistenceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(PERSISTENCE + RETUNING, joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "controllerPoint()", throwing = "e")
    @Override
    public void afterPersistenceThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(PERSISTENCE + THROWING, joinPoint, e);
    }


    // Application layer

    @Pointcut("within(com.miniyus.friday.users.application.service.*)")
    @Override
    public void applicationPoint() {}

    @Before("applicationPoint()")
    @Override
    public void beforeService(JoinPoint joinPoint) throws Throwable {
        beforeLogging(APPLICATION, joinPoint);
    }

    @AfterReturning(pointcut = "applicationPoint()", returning = "returnValue")
    @Override
    public void afterServiceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(APPLICATION + RETUNING, joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "applicationPoint()", throwing = "e")
    @Override
    public void afterServiceThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(APPLICATION + THROWING, joinPoint, e);
    }


    // Domain layer

    @Pointcut("within(com.miniyus.friday.users.domain.*)")
    @Override
    public void domainPoint() {}

    @Before("domainPoint()")
    @Override
    public void beforeDomain(JoinPoint joinPoint) throws Throwable {
        beforeLogging(DOMAIN, joinPoint);
    }

    @AfterReturning(pointcut = "domainPoint()", returning = "returnValue")
    @Override
    public void afterDomainReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(RETUNING, joinPoint, returnValue);
    }

    @AfterThrowing(pointcut = "domainPoint()", throwing = "e")
    @Override
    public void afterDomainThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(THROWING, joinPoint, e);
    }
}
