package com.miniyus.friday.infrastructure.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.*;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * Architecture Layer Aspect Logging.
 *
 * @see HexagonalAspect
 */
@Aspect
@Component
public class ArchitectureLayerAspect extends LoggingAspect implements HexagonalAspect {
    /**
     * Constructor
     *
     * @param objectMapper object mapper for logging to json format
     */
    public ArchitectureLayerAspect(ObjectMapper objectMapper) {
        super(
            "Friday",
            LoggerFactory.getLogger(ArchitectureLayerAspect.class),
            objectMapper);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Pointcut("within(com.miniyus.friday.*.adapter.in..*)")
    public void restAdapterPoint() {
    }

    /**
     * AOP: RestAdapters executing around request
     *
     * @param joinPoint join point.
     * @return return value of RestAdapter
     * @throws Throwable if an error occurs during the execution of the advice
     */
    @Around("restAdapterPoint()")
    public Object aroundRequest(ProceedingJoinPoint joinPoint) throws Throwable {
        return aroundLogging(joinPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Before("restAdapterPoint()")
    public void beforeRequest(JoinPoint joinPoint) {
        beforeLogging("Controller", joinPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterReturning(pointcut = "restAdapterPoint()", returning = "returnValue")
    public void afterRequestReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Controller", joinPoint, returnValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterThrowing(pointcut = "restAdapterPoint()", throwing = "e")
    public void afterRequestThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Controller", joinPoint, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Pointcut(
        "within(com.miniyus.friday.*.adapter.out.persistence..*) ")
    public void persistencePoint() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Before("persistencePoint()")
    public void beforePersistence(JoinPoint joinPoint) {
        beforeLogging("Persistence", joinPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterReturning(pointcut = "persistencePoint()", returning = "returnValue")
    public void afterPersistenceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Persistence", joinPoint, returnValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterThrowing(pointcut = "persistencePoint()", throwing = "e")
    public void afterPersistenceThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Persistence", joinPoint, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Pointcut("within(com.miniyus.friday.*.application..*) ")
    public void applicationPoint() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Before("applicationPoint()")
    public void beforeService(JoinPoint joinPoint) {
        beforeLogging("Service", joinPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterReturning(pointcut = "applicationPoint()", returning = "returnValue")
    public void afterServiceReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Service", joinPoint, returnValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterThrowing(pointcut = "applicationPoint()", throwing = "e")
    public void afterServiceThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Service", joinPoint, e);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Pointcut("within(com.miniyus.friday.*.domain..*)")
    public void domainPoint() {
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @Before("domainPoint()")
    public void beforeDomain(JoinPoint joinPoint) {
        beforeLogging("Domain", joinPoint);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterReturning(pointcut = "domainPoint()", returning = "returnValue")
    public void afterDomainReturning(JoinPoint joinPoint, Object returnValue) {
        afterReturningLogging("Domain", joinPoint, returnValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @AfterThrowing(pointcut = "domainPoint()", throwing = "e")
    public void afterDomainThrowing(JoinPoint joinPoint, Exception e) {
        afterThrowingLogging("Domain", joinPoint, e);
    }
}
