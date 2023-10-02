package com.miniyus.friday.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class SearchNotFoundException extends RestErrorException {
    public SearchNotFoundException() {
        super("error.search.notFound", RestErrorCode.NOT_FOUND);
    }
}
