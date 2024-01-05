package com.meteormin.friday.hosts.application.exception;

import com.meteormin.friday.common.error.ErrorCode;
import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class ExistsHostException extends RestErrorException {

    public ExistsHostException() {
        super("host.error.exists", RestErrorCode.CONFLICT);
    }

    public ExistsHostException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
