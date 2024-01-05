package com.meteormin.friday.common.error;

public class ForbiddenErrorException extends RestErrorException {

    public ForbiddenErrorException() {
        super("error.forbidden", RestErrorCode.FORBIDDEN);
    }
}
