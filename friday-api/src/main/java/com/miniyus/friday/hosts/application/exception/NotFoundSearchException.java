package com.miniyus.friday.hosts.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class NotFoundSearchException extends RestErrorException {
    public NotFoundSearchException() {
        super("search.error.notFound", RestErrorCode.NOT_FOUND);
    }
}
