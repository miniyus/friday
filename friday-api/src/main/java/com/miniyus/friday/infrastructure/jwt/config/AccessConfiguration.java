package com.miniyus.friday.infrastructure.jwt.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/04
 */
@AllArgsConstructor
@Data
public class AccessConfiguration {
    private final Long expiration;
    private final String header;
}
