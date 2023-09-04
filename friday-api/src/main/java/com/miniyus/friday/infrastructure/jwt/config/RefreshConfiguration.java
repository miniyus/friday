package com.miniyus.friday.infrastructure.jwt.config;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Refresh Configuration
 *
 * @author miniyus
 * @date 2023/09/04
 */
@AllArgsConstructor
@Data
public class RefreshConfiguration {
    private Long expiration;
    private String header;
}
