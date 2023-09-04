package com.miniyus.friday.infrastructure.auth.login;

import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
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
