package com.miniyus.friday.application.exception;

import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class HostExistsException extends RestErrorException {

    public HostExistsException() {
        super("error.host.exists", RestErrorCode.CONFLICT);
    }

    public HostExistsException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
