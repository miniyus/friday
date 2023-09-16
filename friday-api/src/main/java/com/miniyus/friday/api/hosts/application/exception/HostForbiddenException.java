package com.miniyus.friday.api.hosts.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class HostForbiddenException extends RestErrorException {
    public HostForbiddenException() {
        super("error.host.forbidden", RestErrorCode.FORBIDDEN);
    }
}

