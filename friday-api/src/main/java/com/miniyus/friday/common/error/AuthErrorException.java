package com.miniyus.friday.common.error;

import lombok.Getter;

/**
 * [description]
 *
 * @author seongminyoo
 * @since 2023/08/28
 */
@Getter
public class AuthErrorException extends RestErrorException {
    public AuthErrorException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }

    public AuthErrorException(ErrorCode errorCode) {
        super(errorCode);
    }

    public AuthErrorException(ErrorCode errorCode, String message, Object... args) {
        super(errorCode, message, args);
    }

}
