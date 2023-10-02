package com.miniyus.friday.common.error;

public class ForbiddenErrorException extends RestErrorException {

    public ForbiddenErrorException() {
        super("error.forbidden", RestErrorCode.FORBIDDEN);
    }
}
