package com.miniyus.friday.hosts.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class NotFoundHostException extends RestErrorException {

    public NotFoundHostException() {
        super("host.error.notFound", RestErrorCode.NOT_FOUND);
    }
}
