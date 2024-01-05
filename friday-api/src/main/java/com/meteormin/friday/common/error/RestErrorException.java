package com.meteormin.friday.common.error;

import lombok.Getter;
import org.springframework.context.MessageSource;

import java.util.*;

/**
 * RestErrorException Common Exception for REST API
 *
 * @author meteormin
 * @since 2023/09/01
 */
@Getter
public class RestErrorException extends RuntimeException {

    /**
     * error code.
     */
    @SuppressWarnings("java:S1948")
    private final ErrorCode errorCode;

    /**
     * translate message's arguments.
     */
    protected final List<Object> args = new ArrayList<>();

    /**
     * error details.
     */
    private final Map<String, List<String>> details = new HashMap<>();

    public RestErrorException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RestErrorException(
        String message,
        ErrorCode errorCode,
        Throwable cause
    ) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.name(), cause);
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        Collections.addAll(this.args, args);
    }

    public RestErrorException(
        ErrorCode errorCode,
        String message,
        Throwable cause,
        Object... args) {
        super(message, cause);
        this.errorCode = errorCode;
        Collections.addAll(this.args, args);
    }

    public static RestErrorException validationFail(String message,
        Map<String, List<String>> details) {
        var errorCode = RestErrorCode.BAD_REQUEST;
        var ex = new RestErrorException(errorCode, message);
        ex.setDetails(details);
        return ex;
    }

    public static RestErrorException validationFail(String message,
        Map<String, List<String>> details, Throwable cause) {
        var errorCode = RestErrorCode.BAD_REQUEST;
        var ex = new RestErrorException(errorCode, message, cause);
        ex.setDetails(details);
        return ex;
    }

    public ErrorResponse getBody(MessageSource messageSource, Locale locale) {
        String message;
        if (getArgs() == null) {
            message = messageSource.getMessage(
                getMessage(),
                null,
                getMessage(),
                locale);
        } else {
            message = messageSource.getMessage(
                getMessage(),
                getArgs().toArray(Object[]::new),
                getMessage(),
                locale);
        }

        Map<String, List<String>> tempDetails = null;
        if (!getDetails().isEmpty()) {
            var origin = getDetails();
            tempDetails = new HashMap<>();
            for (Map.Entry<String, List<String>> msg : origin.entrySet()) {
                List<String> translated = new ArrayList<>();
                for (String v : msg.getValue()) {
                    translated.add(messageSource.getMessage(
                        v, null, v, locale));
                }
                tempDetails.put(msg.getKey(), translated);
            }
        }

        return new ErrorResponse(
            getErrorCode(),
            message != null ? message : getMessage(),
            tempDetails
        );
    }

    protected void setDetails(Map<String, List<String>> details) {
        if(!this.details.isEmpty()) {
            this.details.clear();
        }
        
        for (Map.Entry<String, List<String>> msg : details.entrySet()) {
            for (String v : msg.getValue()) {
                this.details.put(msg.getKey(), Collections.singletonList(v));
            }
        }
    }
}
