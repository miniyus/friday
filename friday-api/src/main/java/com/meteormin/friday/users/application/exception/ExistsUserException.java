package com.meteormin.friday.users.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class ExistsUserException extends RestErrorException {

    public ExistsUserException() {
        super("error.user.exists", RestErrorCode.CONFLICT);
    }
}
