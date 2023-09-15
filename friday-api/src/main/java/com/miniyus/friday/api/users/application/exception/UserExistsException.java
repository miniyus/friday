package com.miniyus.friday.api.users.application.exception;

import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class UserExistsException extends RestErrorException {

    public UserExistsException() {
        super("error.user.exists", RestErrorCode.CONFLICT);
    }

    public UserExistsException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
