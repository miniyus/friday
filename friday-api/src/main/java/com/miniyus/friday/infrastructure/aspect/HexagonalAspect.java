package com.miniyus.friday.infrastructure.aspect;

import org.aspectj.lang.JoinPoint;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/11
 */
public interface HexagonalAspect {

    String REQUEST = "Request";
    String PERSISTENCE = "Persistence";
    String APPLICATION = "Application";
    String DOMAIN = "Domain";

    // Adapter layer

    // in/rest
    void controllerPoint();

    void beforeRequest(JoinPoint joinPoint) throws Throwable;

    void afterRequestReturning(JoinPoint joinPoint, Object returnValue);

    void afterRequestThrowing(JoinPoint joinPoint, Exception e) throws Throwable;

    // out/persistence
    void persistencePoint();

    void beforePersistence(JoinPoint joinPoint) throws Throwable;

    void afterPersistenceReturning(JoinPoint joinPoint, Object returnValue);

    void afterPersistenceThrowing(JoinPoint joinPoint, Exception e) throws Throwable;

    // Application layer
    void applicationPoint();

    void beforeService(JoinPoint joinPoint) throws Throwable;

    void afterServiceReturning(JoinPoint joinPoint, Object returnValue);

    void afterServiceThrowing(JoinPoint joinPoint, Exception e) throws Throwable;

    // Domain layer
    void domainPoint();

    void beforeDomain(JoinPoint joinPoint) throws Throwable;

    void afterDomainReturning(JoinPoint joinPoint, Object returnValue);

    void afterDomainThrowing(JoinPoint joinPoint, Exception e) throws Throwable;
}
