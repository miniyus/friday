package com.miniyus.friday.infrastructure.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.common.error.ErrorCode;

import io.micrometer.common.lang.Nullable;
import lombok.Getter;

/**
 * 
 * Error Response for REST API
 * - timestamp: responsed at
 * - error: error summary
 * - message: error description
 * - details: error details
 * 
 * @author miniyus
 * @date 2023/08/30
 */
@Getter
public class ErrorResponse {
    private LocalDateTime timestamp;
    private int code;
    private String error;
    private String message;

    @Nullable
    private HashMap<String, List<String>> details;

    public ErrorResponse(ErrorCode errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(ErrorCode errorCode, String message, HashMap<String, List<String>> details) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = details;
    }

    public ErrorResponse(AuthErrorCode errorCode, String message) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(AuthErrorCode errorCode, String message, HashMap<String, List<String>> details) {
        this.timestamp = LocalDateTime.now();
        this.code = errorCode.getErrorCode();
        this.error = errorCode.name();
        this.message = message;
        this.details = details;
    }
}
