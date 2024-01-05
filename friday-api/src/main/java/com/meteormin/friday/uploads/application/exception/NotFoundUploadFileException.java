package com.meteormin.friday.uploads.application.exception;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;

public class NotFoundUploadFileException extends RestErrorException {

    public NotFoundUploadFileException() {
        super(RestErrorCode.NOT_FOUND, "upload.error.notFound");
    }
}
