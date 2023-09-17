package com.miniyus.friday.infrastructure.security.auth;

/**
 * PasswordAuthentication
 *
 * password authentication user info
 * 
 * @author seongminyoo
 * @date 2023/09/04
 */
public record PasswordAuthentication(
    String email,
    String password
) {
}
