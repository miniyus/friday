package com.miniyus.friday.users.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class ExistsUserException extends RestErrorException {

    public ExistsUserException() {
        super("error.user.exists", RestErrorCode.CONFLICT);
    }
}
