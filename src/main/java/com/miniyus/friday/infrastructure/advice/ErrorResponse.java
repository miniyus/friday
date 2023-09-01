package com.miniyus.friday.infrastructure.advice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;

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
    private String error;
    private String message;

    @Nullable
    private HashMap<String, List<String>> details;

    public ErrorResponse(LocalDateTime timestamp, String error, String message) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.details = null;
    }

    public ErrorResponse(LocalDateTime timestamp, String error, String message, HashMap<String, List<String>> details) {
        this.timestamp = timestamp;
        this.error = error;
        this.message = message;
        this.details = details;
    }
}
