package com.miniyus.friday.uploads.application.exception;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;

public class NotFoundUploadFileException extends RestErrorException {

    public NotFoundUploadFileException() {
        super(RestErrorCode.NOT_FOUND, "upload.error.notFound");
    }
}
