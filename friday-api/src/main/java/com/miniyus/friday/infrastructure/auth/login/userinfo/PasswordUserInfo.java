package com.miniyus.friday.infrastructure.auth.login.userinfo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Getter
@AllArgsConstructor
@Builder
public class PasswordUserInfo {
    private Long id;

    private String email;

    private String password;

    private String name;

    private String role;
}
