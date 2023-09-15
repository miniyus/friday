package com.miniyus.friday.api.users.adapter.in.rest.request;

import com.miniyus.friday.api.users.application.port.in.usecase.UpdateUserCommand;
import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.common.validation.annotation.NullOrNotBlank;
import com.miniyus.friday.infrastructure.security.UserRole;
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
public record PatchUserRequest(
        @NullOrNotBlank(message = "validation.user.name.notBlank") @Size(min = 2, max = 50,
                message = "validation.user.name.size") String name,

        @NullOrNotBlank(message = "validation.user.role.notBlank") @Enum(enumClass = UserRole.class,
                message = "validation.user.role.enum", ignoreCase = true) String role)
        implements Serializable {

    public UpdateUserCommand toCommand(Long id) {
        return UpdateUserCommand.builder()
                .id(id)
                .name(name)
                .role(role)
                .build();
    }
}