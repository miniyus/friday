package com.miniyus.friday.hosts.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class ExistsSearchException extends RestErrorException {
    public ExistsSearchException() {
        super("search.error.exists", RestErrorCode.CONFLICT);
    }
}
