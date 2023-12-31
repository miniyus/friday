package com.meteormin.friday.auth.domain;

import com.meteormin.friday.users.domain.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class Auth {
    private Long id;
    private String email;
    private String name;
    private String password;
    private UserRole role;
    private String snsId;
    private String provider;

    public static Auth signup(
        String email,
        String password,
        String name,
        UserRole role
    ) {
        return Auth.builder()
            .email(email)
            .password(password)
            .name(name)
            .role(role)
            .build();
    }
}
