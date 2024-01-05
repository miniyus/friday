package com.meteormin.friday.common.error;

import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * Error Response for REST API
 * <ul>
 *     <li> code: error code </li>
 *     <li> message: error message </li>
 *     <li> details: error details </li>
 *     <li> timestamp: response at</li>
 * </ul>
 *
 * @author seongminyoo
 * @since 2023/08/30
 */
@Getter
public class ErrorResponse {
    private final LocalDateTime timestamp;
    private final int code;
    private final String error;
    private final String message;

    @Nullable
    private final Map<String, List<String>> details;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(
        @NonNull ErrorCode errorCode,
        @NonNull String message,
        @Nullable Map<String, List<String>> details) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = details;
    }
}
