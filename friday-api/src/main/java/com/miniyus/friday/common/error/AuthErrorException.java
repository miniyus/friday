package com.miniyus.friday.common.error;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/28
 */
@Getter
public class AuthErrorException extends RestErrorException {
    protected List<Object> args;

    public AuthErrorException(String message, AuthErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthErrorException(AuthErrorCode errorCode) {
        super(errorCode);
    }

    public AuthErrorException(AuthErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

}
