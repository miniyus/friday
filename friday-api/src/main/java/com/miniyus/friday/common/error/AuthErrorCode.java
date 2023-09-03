package com.miniyus.friday.common.error;

import org.springframework.http.HttpStatus;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/28
 */
public enum AuthErrorCode {
    ACCESS_DENINED(1, 401),
    INSUFFICIENT_SCOPE(2, 403),
    INVALID_CLIENT(3, 400),
    INVALID_GRANT(4, 400),
    INVALID_REDIRECT_URI(5, 400),
    INVALID_REQUEST(6, 400),
    INVALID_SCOPE(7, 400),
    INVALID_TOKEN(8, 400),
    SERVER_ERROR(9, 500),
    TEMPORARILY_UNAVAILABLE(10, 503),
    UNAUTHORIZED_CLIENT(11, 401),
    UNSUPPORTED_GRANT_TYPE(12, 400),
    UNSUPPORTED_RESPONSE_TYPE(13, 400),
    UNSUPPORTED_TOKEN_TYPE(14, 400);

    private final int errorCode;
    private final int statusCode;

    AuthErrorCode(int errorCode, int statusCode) {
        this.errorCode = errorCode;
        this.statusCode = statusCode;
    }

    /**
     * @return the errorCode
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * @return the statusCode
     */
    public int getStatusCode() {
        return statusCode;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(statusCode);
    }
}
