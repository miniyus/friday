package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.common.validation.annotation.NullOrNotBlank;
import com.miniyus.friday.infrastructure.security.UserRole;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserCommand;
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
public class PatchUserRequest {
    @NullOrNotBlank(message = "validation.user.name.notBlank")
    @Size(min = 2, max = 50, message = "validation.user.name.size")
    private String name;

    @NullOrNotBlank(message = "validation.user.role.notBlank")
    @Enum(enumClass = UserRole.class, message = "validation.user.role.enum", ignoreCase = true)
    private String role;

    public UpdateUserCommand toCommand(Long id) {
        return UpdateUserCommand.builder()
                .id(id)
                .name(name)
                .role(role)
                .build();
    }
}
