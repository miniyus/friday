package com.miniyus.friday.infrastructure.aspect;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class HostAspect extends LoggingAspect implements HexagonalAspect {

    public HostAspect() {
        super("Host", LoggerFactory.getLogger(HostAspect.class));
    }


    @Override
    @Pointcut("within(com.miniyus.friday.adapter.in.rest.HostController)")
    public void controllerPoint() {
    }

    @Override
    @Before("controllerPoint()")
    public void beforeRequest(JoinPoint joinPoint) throws Throwable {
        beforeLogging(REQUEST, joinPoint);
    }

    @Override
    @AfterReturning(pointcut = "controllerPoint()", returning = "returnValue")
    public void afterRequestReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(REQUEST, joinPoint, returnValue);
    }

    @Override
    @AfterThrowing(pointcut = "controllerPoint()", throwing = "e")
    public void afterRequestThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(REQUEST, joinPoint, e);
    }

    @Override
    @Pointcut("within(com.miniyus.friday.adapter.out.persistence.HostAdapter)")
    public void persistencePoint() {
    }

    @Override
    @Before("persistencePoint()")
    public void beforePersistence(JoinPoint joinPoint) throws Throwable {
        beforeLogging(PERSISTENCE, joinPoint);
    }

    @Override
    @AfterReturning(pointcut = "persistencePoint()", returning = "returnValue")
    public void afterPersistenceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(PERSISTENCE, joinPoint, returnValue);
    }

    @Override
    @AfterThrowing(pointcut = "persistencePoint()", throwing = "e")
    public void afterPersistenceThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(PERSISTENCE, joinPoint, e);
    }

    @Override
    @Pointcut("within(com.miniyus.friday.application.HostService)")
    public void applicationPoint() {
    }

    @Override
    @Before("applicationPoint()")
    public void beforeService(JoinPoint joinPoint) throws Throwable {
        beforeLogging(APPLICATION, joinPoint);
    }

    @Override
    @AfterReturning(pointcut = "applicationPoint()", returning = "returnValue")
    public void afterServiceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(APPLICATION, joinPoint, returnValue);
    }

    @Override
    @AfterThrowing(pointcut = "applicationPoint()", throwing = "e")
    public void afterServiceThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(APPLICATION, joinPoint, e);
    }

    @Override
    @Pointcut("within(com.miniyus.friday.domain.hosts..*)")
    public void domainPoint() {
    }

    @Override
    @Before("domainPoint()")
    public void beforeDomain(JoinPoint joinPoint) throws Throwable {
        beforeLogging(DOMAIN, joinPoint);
    }

    @Override
    @AfterReturning(pointcut = "domainPoint()", returning = "returnValue")
    public void afterDomainReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging(DOMAIN, joinPoint, returnValue);
    }

    @Override
    @AfterThrowing(pointcut = "domainPoint()", throwing = "e")
    public void afterDomainThrowing(JoinPoint joinPoint, Exception e) throws Throwable {
        afterThrowingLogging(DOMAIN, joinPoint, e);
    }
}
