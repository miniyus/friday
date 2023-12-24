package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Builder
public record CreateUserRequest(
    @NotBlank(message = "validation.user.email.notBlank") @Email(
        message = "validation.user.email.email") String email,

    @NotBlank(message = "validation.user.password.notBlank") @Size(min = 8, max = 50,
        message = "validation.user.password.size") String password,

    @NotBlank(message = "validation.user.name.notBlank") @Size(min = 2, max = 20,
        message = "validation.user.name.size") String name,

    @NotBlank(message = "validation.user.role.notBlank") @Enum(enumClass = UserRole.class,
        message = "validation.user.role.enum",
        ignoreCase = true) String role) implements Serializable {
    public User toDomain() {
        return User.create(
            email,
            password,
            name,
            role,
            null,
            null
        );
    }
}
