package com.miniyus.friday.infrastructure.security.oauth2.exception;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/31
 */
public class NotSupportProviderException extends RuntimeException {
    public NotSupportProviderException(String message) {
        super(message);
    }
}
