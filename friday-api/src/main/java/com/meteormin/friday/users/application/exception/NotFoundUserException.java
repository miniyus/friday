package com.meteormin.friday.users.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class NotFoundUserException extends RestErrorException {
    public NotFoundUserException() {
        super("error.user.notFound", RestErrorCode.NOT_FOUND);
    }
}
