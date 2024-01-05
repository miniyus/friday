package com.meteormin.friday.common.request.exception;

/**
 * Exception for resize image
 * @see com.meteormin.friday.common.util.UploadFileUtil
 */
public class ResizeImageException extends RuntimeException {
    public ResizeImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
