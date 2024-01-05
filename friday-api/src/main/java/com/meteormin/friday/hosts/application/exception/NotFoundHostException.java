package com.meteormin.friday.hosts.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class NotFoundHostException extends RestErrorException {

    public NotFoundHostException() {
        super("host.error.notFound", RestErrorCode.NOT_FOUND);
    }
}
