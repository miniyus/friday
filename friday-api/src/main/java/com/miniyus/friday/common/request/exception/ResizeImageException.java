package com.miniyus.friday.common.request.exception;

/**
 * Exception for resize image
 * @see com.miniyus.friday.common.util.UploadFileUtil
 */
public class ResizeImageException extends RuntimeException {
    public ResizeImageException(String message, Throwable cause) {
        super(message, cause);
    }
}
