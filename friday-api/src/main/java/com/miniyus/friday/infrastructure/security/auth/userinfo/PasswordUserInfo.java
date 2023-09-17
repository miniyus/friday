package com.miniyus.friday.infrastructure.security.auth.userinfo;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.infrastructure.security.UserRole;

import java.io.Serializable;

/**
 * password authentication user info
 *
 * @author seongminyoo
 * @date 2023/09/04
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
