package com.meteormin.friday.hosts.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class ExistsSearchException extends RestErrorException {
    public ExistsSearchException() {
        super("search.error.exists", RestErrorCode.CONFLICT);
    }
}
