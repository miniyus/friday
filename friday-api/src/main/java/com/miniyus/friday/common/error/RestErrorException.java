package com.miniyus.friday.common.error;

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

    public RestErrorException(String message, ErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public RestErrorException(ErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    /**
     * Retrieves the error code associated with this object.
     *
     * @return the error code
     */
    public ErrorCode getErrorCode() {
        return this.errorCode;
    }
}
