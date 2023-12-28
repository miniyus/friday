package com.miniyus.friday.users.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class NotFoundUserException extends RestErrorException {
    public NotFoundUserException() {
        super("error.user.notFound", RestErrorCode.NOT_FOUND);
    }
}
