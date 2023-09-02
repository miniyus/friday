package com.miniyus.friday.common.error;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/28
 */
public class AuthErrorException extends RuntimeException {
    protected final AuthErrorCode errorCode;

    public AuthErrorException(String message, AuthErrorCode errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public AuthErrorCode getErrorCode() {
        return errorCode;
    }
}
