package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.infrastructure.auth.UserRole;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserCommand;
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
public class UpdateUserRequest {
    @NotBlank
    @Size(min = 2, max = 50)
    private String name;

    @NotBlank
    @Enum(enumClass = UserRole.class)
    private String role;

    public UpdateUserCommand toCommand(Long id) {
        return UpdateUserCommand.builder()
                .id(id)
                .name(name)
                .role(role)
                .build();
    }
}
