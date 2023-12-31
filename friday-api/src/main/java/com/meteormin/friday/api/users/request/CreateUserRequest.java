package com.meteormin.friday.api.users.request;

import com.meteormin.friday.common.validation.annotation.Enum;
import com.meteormin.friday.users.domain.CreateUser;
import com.meteormin.friday.users.domain.UserRole;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * Create User Request.
 *
 * @author meteormin
 * @since 2023/09/02
 */
@Builder
public record CreateUserRequest(
    @NotBlank(message = "validation.user.email.notBlank") @Email(
        message = "validation.user.email.email")
    String email,

    @NotBlank(message = "validation.user.password.notBlank") @Size(min = 8, max = 50,
        message = "validation.user.password.size")
    String password,

    @NotBlank(message = "validation.user.name.notBlank") @Size(min = 2, max = 20,
        message = "validation.user.name.size")
    String name,

    @NotBlank(message = "validation.user.role.notBlank") @Enum(enumClass = UserRole.class,
        message = "validation.user.role.enum",
        ignoreCase = true)
    String role) implements Serializable {
    public CreateUser toDomain() {
        return CreateUser.builder()
            .email(email)
            .password(password)
            .name(name)
            .role(UserRole.of(role, true))
            .build();
    }
}
