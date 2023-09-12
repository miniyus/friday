package com.miniyus.friday.common.error;

import java.util.Arrays;
import java.util.List;

/**
 * RestErrorException
 * 
 * Common Exception for REST API
 *
 * @author miniyus
 * @date 2023/09/01
 */
public class RestErrorException extends RuntimeException {

    private final ErrorCode errorCode;

    protected List<Object> args;

    public RestErrorException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = Arrays.asList(args);
    }

    /**
     * Retrieves the error code associated with this object.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public List<Object> getArgs() {
        return this.args;
    }
}
