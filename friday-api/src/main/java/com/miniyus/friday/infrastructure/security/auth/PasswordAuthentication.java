package com.miniyus.friday.infrastructure.security.auth;

/**
 * PasswordAuthentication
 * <p>
 *  password authentication user info
 * </p>
 *
 * @author seongminyoo
 * @since 2023/09/04
 */
public record PasswordAuthentication(
    String email,
    String password,
    String secret
) {
}
