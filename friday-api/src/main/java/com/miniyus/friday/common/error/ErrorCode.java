package com.miniyus.friday.common.error;

import org.springframework.http.HttpStatus;

/**
 * ErrorCode
 * 
 * common error codes
 *
 * @author miniyus
 * @date 2023/09/01
 */
public enum ErrorCode {
    BAD_REQUEST(10, 400),
    UNAUTHORIZED(11, 401),
    FORBIDDEN(12, 403),
    NOT_FOUND(13, 404),
    METHOD_NOT_ALLOWED(14, 405),
    CONFLICT(15, 409),
    TOO_MANY_REQUESTS(16, 429),
    INTERNAL_SERVER_ERROR(20, 500),
    SERVER_UNAVAILABLE(21, 503);

    private final int code;
    private final int status;

    ErrorCode(int code, int status) {
        this.code = code;
        this.status = status;
    }

    /**
     * Retrieves the code value.
     *
     * @return the code value
     */
    public int getCode() {
        return code;
    }

    /**
     * A description of the entire Java function.
     *
     * @param None No parameters are accepted by this function.
     * @return int The status value of the function.
     */
    public int getStatus() {
        return status;
    }

    public HttpStatus getHttpStatus() {
        return HttpStatus.valueOf(status);
    }

    /**
     * A function to retrieve an ErrorCode object based on a given code.
     *
     * @param code the code to match against the ErrorCode objects
     * @return the ErrorCode object matching the given code, or null if no match is
     *         found
     */
    public static ErrorCode fromCode(int code) {
        for (ErrorCode e : ErrorCode.values()) {
            if (e.getCode() == code) {
                return e;
            }
        }
        return null;
    }

    /**
     * Finds and returns the ErrorCode object that corresponds to the given status.
     *
     * @param status the status to search for
     * @return the ErrorCode object that matches the given status, or null if no
     *         match is found
     */
    public static ErrorCode fromStatus(int status) {
        for (ErrorCode e : ErrorCode.values()) {
            if (e.getStatus() == status) {
                return e;
            }
        }
        return null;
    }
}
