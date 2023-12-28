package com.miniyus.friday.hosts.application.exception;

import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class ExistsHostException extends RestErrorException {

    public ExistsHostException() {
        super("host.error.exists", RestErrorCode.CONFLICT);
    }

    public ExistsHostException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
