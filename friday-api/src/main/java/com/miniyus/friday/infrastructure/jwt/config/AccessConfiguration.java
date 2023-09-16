package com.miniyus.friday.infrastructure.jwt.config;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/04
 */
public record AccessConfiguration(Long expiration, String header) {
}
