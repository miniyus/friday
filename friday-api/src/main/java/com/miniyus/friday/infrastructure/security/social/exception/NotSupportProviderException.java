package com.miniyus.friday.infrastructure.security.social.exception;

import lombok.Getter;

/**
 * not support provider exception.
 *
 * @author miniyus
 * @since 2023/08/31
 */
@Getter
public class NotSupportProviderException extends RuntimeException {
    private final String provider;

    public NotSupportProviderException(String message, String provider) {
        super(message);
        this.provider = provider;
    }
}
