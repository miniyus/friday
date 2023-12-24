package com.miniyus.friday.hosts.application.exception;

import com.miniyus.friday.common.error.ErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class HostNotFoundException extends RestErrorException {

    public HostNotFoundException() {
        super("error.host.notFound", RestErrorCode.NOT_FOUND);
    }

    public HostNotFoundException(String message, ErrorCode errorCode) {
        super(message, errorCode);
    }
}
