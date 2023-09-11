package com.miniyus.friday.infrastructure.aspect;

import org.slf4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.reflect.MethodSignature;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/11
 */
public abstract class AbstractAspect implements HexagonalAspect {
    protected String tag;

    public static String REQUEST = "Request";

    public static String PERSISTENCE = "Persistence";

    public static String APPLICATION = "Application";

    public static String DOMAIN = "Domain";

    public static String RETUNING = "Returning";

    public static String THROWING = "Throwing";

    protected final Logger log;

    public AbstractAspect(String tag, Logger log) {
        this.tag = tag;
        this.log = log;
    }

    private String parseSignature(JoinPoint joinPoint) {
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = methodSignature.getName();
        String[] names = methodSignature.getParameterNames();
        Object[] args = joinPoint.getArgs();

        // 로깅 형식에 맞게 출력
        StringBuilder logMessage = new StringBuilder();
        logMessage.append(className).append(".")
                .append(methodName).append("(");

        // 메소드 파라미터 추가
        for (int i = 0; i < args.length; i++) {
            logMessage.append(names[i])
                    .append("=")
                    .append(args[i]);
            if (i < args.length - 1) {
                logMessage.append(", ");
            }
        }

        logMessage.append(")");

        return logMessage.toString();
    }

    protected void loggingJoinPoint(String when, String action, JoinPoint joinPoint) {
        String format = String.format("[%s%s] {} {}", when, action);

        log.info(format, tag, parseSignature(joinPoint));
    }

    protected void loggingJoinPoint(String when, String action, JoinPoint joinPoint,
            Object returnValue) {
        String format = String.format("[%s%s] {} {}", when, action);

        log.info(format, tag, parseSignature(joinPoint));
        if (returnValue == null) {
            return;
        }

        log.info("\treturnValue: {}", returnValue);
    }

    protected void loggingJoinPoint(String when, String action, JoinPoint joinPoint,
            Exception e) {
        String format = String.format("[%s%s] {} {}", when, action);

        log.error(format, tag, parseSignature(joinPoint));
        log.error("\tmessage: {}", e.getMessage());
    }

    void beforeLogging(String action, JoinPoint joinPoint) {
        loggingJoinPoint("before", action, joinPoint);
    }

    void afterReturningLogging(String action, JoinPoint joinPoint, Object returnValue) {
        loggingJoinPoint("after", action, joinPoint, returnValue);
    }

    void afterThrowingLogging(String action, JoinPoint joinPoint, Exception e) {
        loggingJoinPoint("after", action, joinPoint, e);
    }
}
