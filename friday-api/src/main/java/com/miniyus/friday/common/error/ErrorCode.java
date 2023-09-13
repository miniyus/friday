package com.miniyus.friday.common.error;

import org.springframework.http.HttpStatus;

public interface ErrorCode {
    int getErrorCode();

    HttpStatus getHttpStatus();

    int getStatusCode();

    String name();
}
