package com.miniyus.friday.application.exception;

import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class UserNotFoundException extends RestErrorException {
    public UserNotFoundException() {
        super("error.user.notFound", RestErrorCode.NOT_FOUND);
    }
    public UserNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
