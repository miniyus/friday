package com.miniyus.friday.infrastructure.security.auth;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * PasswordAuthentication
 *
 * password authentication user info
 * 
 * @author seongminyoo
 * @date 2023/09/04
 */
@NoArgsConstructor
@Getter
public class PasswordAuthentication {
    private String username;

    private String password;

    public PasswordAuthentication(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
