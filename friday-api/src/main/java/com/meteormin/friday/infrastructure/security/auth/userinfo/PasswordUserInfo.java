package com.meteormin.friday.infrastructure.security.auth.userinfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * password authentication user info
 *
 * @author seongminyoo
 * @since 2023/09/04
 */
@Builder
public record PasswordUserInfo(
    @Email
    @NotBlank
    String email,
    @NotBlank
    @Size(min = 8, max = 50)
    String password,

    @NotBlank
    @Size(min = 2, max = 50)
    String name
) implements Serializable {
}
