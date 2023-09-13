package com.miniyus.friday.common.error;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * RestErrorException
 * Common Exception for REST API
 *
 * @author miniyus
 * @date 2023/09/01
 */
@Getter
public class RestErrorException extends RuntimeException {

    /**
     * -- GETTER --
     *  Retrieves the error code associated with this object.
     *
     */
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

}
