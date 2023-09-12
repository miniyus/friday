package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.infrastructure.auth.UserRole;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    @Size(min = 8, max = 50)
    private String password;

    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Enum(enumClass = UserRole.class)
    private String role;

    public CreateUserCommand toCommand() {
        return CreateUserCommand.builder()
                .email(email)
                .password(password)
                .name(name)
                .role(role)
                .build();
    }
}
