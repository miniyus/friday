package com.miniyus.friday.common.error;

import org.springframework.http.HttpStatus;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/28
 */
public enum AuthErrorCode {
    ACCESS_DENIED(1, 401), // access denied
    INSUFFICIENT_SCOPE(2, 403), // insufficient scope
    INVALID_CLIENT(3, 400), // invalid client
    INVALID_GRANT(4, 400), // invalid grant
    INVALID_REDIRECT_URI(5, 400), // invalid redirect uri
    INVALID_REQUEST(6, 400), // invalid request
    INVALID_SCOPE(7, 400), // invalid scope
    INVALID_TOKEN(8, 400), // invalid token
    SERVER_ERROR(9, 500), // server error
    TEMPORARILY_UNAVAILABLE(10, 503), // temporary unavailable
    UNAUTHORIZED_CLIENT(11, 401), // unauthorized client
    UNSUPPORTED_GRANT_TYPE(12, 400), // unsupported grant type
    UNSUPPORTED_RESPONSE_TYPE(13, 400), // unsupported response type
    UNSUPPORTED_TOKEN_TYPE(14, 400); // unsupported token type

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
