package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CreateUserRequest {
    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    @NotBlank
    private String name;

    @NotBlank
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
