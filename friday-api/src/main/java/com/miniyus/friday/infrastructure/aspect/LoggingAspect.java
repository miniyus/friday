package com.miniyus.friday.infrastructure.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.io.IOUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * logging aspect
 *
 * @author seongminyoo
 * @since 2023/09/11
 */
public abstract class LoggingAspect {
    protected String tag;

    public static final String RETUNING = "Returning";

    public static final String THROWING = "Throwing";

    protected final Logger log;

    protected final ObjectMapper objectMapper;

    private static final String DEFAULT_LOG_FORMAT = "[%s%s] {} {}";

    protected LoggingAspect(String tag, Logger log, ObjectMapper objectMapper) {
        this.tag = tag;
        this.log = log;
        this.objectMapper = objectMapper;
    }

    protected Object aroundLogging(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
            ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
                .getRequest();
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        PrincipalUserInfo userInfo = null;
        if (principal instanceof UserDetails) {
            userInfo = (PrincipalUserInfo) principal;
        }

        Map<String, String[]> paramMap = request.getParameterMap();
        String params = "";
        if (!paramMap.isEmpty()) {
            params = " [" + paramMapToString(paramMap) + "]";
        }

        long start = System.currentTimeMillis();
        ResponseEntity<?> response;
        try {
            response = (ResponseEntity<?>) joinPoint.proceed(joinPoint.getArgs());
        } finally {
            long end = System.currentTimeMillis();
            log.info("[REST API] {}", tag);
            log.info("Request {} {}{} < {} ({}ms)", request.getMethod(),
                request.getRequestURI(),
                params, request.getRemoteHost(), end - start);

            var authorization = request.getHeader(HttpHeaders.AUTHORIZATION);
            log.info("Authorization {}", authorization);

            var userAgent = request.getHeader(HttpHeaders.USER_AGENT);
            log.info("User-Agent {}", userAgent);

            if (userInfo != null) {
                var client = userInfo.getProvider().value();
                var id = userInfo.getId();
                log.info("Auth: Id={}, Client={}", id, client);
            }

            log.info("Query {}", request.getQueryString());
            if (request.getParameterMap() != null) {
                params = toJson(request.getParameterMap());
                log.info("Params {}", params);
            }
            if (request.getReader() != null) {
                var body = IOUtils.toString(request.getReader());
                log.info("Body {}", body);
            }
        }

        if (response != null) {
            log.info("ResponseStatus {}", response.getStatusCode());
            var responseBody = toJson(response.getBody());
            log.info("ResponseBody {}", responseBody);
        } else {
            log.info("ResponseStatus {}", HttpStatus.NO_CONTENT);
            log.info("ResponseBody {}", HttpStatus.NO_CONTENT.getReasonPhrase());
        }

        return response;
    }

    void beforeLogging(String action, JoinPoint joinPoint) {
        loggingJoinPoint(action, joinPoint);
    }

    void afterReturningLogging(String action, JoinPoint joinPoint, Object returnValue) {
        loggingJoinPoint(action + RETUNING, joinPoint, returnValue);
    }

    void afterThrowingLogging(String action, JoinPoint joinPoint, Exception e) {
        loggingJoinPoint(action + THROWING, joinPoint, e);
    }

    protected void loggingJoinPoint(String action, JoinPoint joinPoint) {
        String format = String.format(DEFAULT_LOG_FORMAT, "before", action);
        var signature = parseSignature(joinPoint);
        log.info(format, tag, signature);
    }

    protected void loggingJoinPoint(
        String action,
        JoinPoint joinPoint,
        Object returnValue) {
        String format = String.format(DEFAULT_LOG_FORMAT, "after", action);
        var signature = parseSignature(joinPoint);
        log.info(format, tag, signature);
        if (returnValue == null) {
            return;
        }

        var returnJson = toJson(returnValue);
        log.info("ReturnValue {}", returnJson);
    }

    protected void loggingJoinPoint(
        String action,
        JoinPoint joinPoint,
        Exception e) {

        String format = String.format(DEFAULT_LOG_FORMAT, "after", action);
        var signature = parseSignature(joinPoint);
        log.error(format, tag, signature);
        log.error("Message {}", e.getMessage(), e);
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

    protected String toJson(Object obj) {
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (IOException e) {
            log.error("failed mapping object", e);
            log.info("{}", obj);
        }

        return obj.toString();
    }

    protected String paramMapToString(Map<String, String[]> paramMap) {
        return paramMap.entrySet().stream()
            .map(entry -> String.format("%s -> (%s)",
                entry.getKey(), String.join(", ", entry.getValue())))
            .collect(Collectors.joining(", "));
    }

}
