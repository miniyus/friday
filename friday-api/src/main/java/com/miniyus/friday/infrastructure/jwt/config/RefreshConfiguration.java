package com.miniyus.friday.infrastructure.jwt.config;

/**
 * Refresh Configuration
 *
 * @author miniyus
 * @date 2023/09/04
 */
public record RefreshConfiguration(Long expiration, String header) {
}
