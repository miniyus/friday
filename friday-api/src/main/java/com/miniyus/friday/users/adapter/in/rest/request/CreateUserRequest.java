package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.infrastructure.security.UserRole;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Create user request
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank(message = "validation.user.email.notBlank")
    @Email(message = "validation.user.email.email")
    private String email;

    @NotBlank(message = "validation.user.password.notBlank")
    @Size(min = 8, max = 50, message = "validation.user.password.size")
    private String password;

    @NotBlank(message = "validation.user.name.notBlank")
    @Size(min = 2, max = 20, message = "validation.user.name.size")
    private String name;

    @NotBlank(message = "validation.user.role.notBlank")
    @Enum(enumClass = UserRole.class,
            message = "validation.user.role.enum",
            ignoreCase = true)
    private String role;

    /**
     * Converts the current object to a CreateUserCommand object.
     *
     * @return a new CreateUserCommand object with the values from the current object
     */
    public CreateUserCommand toCommand() {
        return CreateUserCommand.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }
}
