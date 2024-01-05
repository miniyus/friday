package com.meteormin.friday.infrastructure.filesystem.exception;

public class FileNotFoundException extends RuntimeException {

    public FileNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
