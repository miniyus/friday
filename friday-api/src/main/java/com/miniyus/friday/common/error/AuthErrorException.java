package com.miniyus.friday.common.error;

import java.util.Arrays;
import java.util.List;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/28
 */
public class AuthErrorException extends RuntimeException {
    protected final AuthErrorCode errorCode;

    protected List<Object> args;

    public AuthErrorException(String message, AuthErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthErrorException(AuthErrorCode errorCode) {
        super(errorCode.name());
        this.errorCode = errorCode;
    }

    public AuthErrorException(AuthErrorCode errorCode, String message, Object... args) {
        super(message);
        this.errorCode = errorCode;
        this.args = Arrays.asList(args);
    }

    public AuthErrorCode getErrorCode() {
        return errorCode;
    }

    public List<Object> getArgs() {
        return this.args;
    }
}
