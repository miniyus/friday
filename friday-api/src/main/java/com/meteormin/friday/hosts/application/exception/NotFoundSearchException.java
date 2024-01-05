package com.meteormin.friday.hosts.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class NotFoundSearchException extends RestErrorException {
    public NotFoundSearchException() {
        super("search.error.notFound", RestErrorCode.NOT_FOUND);
    }
}
