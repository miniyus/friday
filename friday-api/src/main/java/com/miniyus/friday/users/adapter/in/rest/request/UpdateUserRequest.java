package com.miniyus.friday.users.adapter.in.rest.request;

import com.miniyus.friday.common.validation.annotation.Enum;
import com.miniyus.friday.common.validation.annotation.NullOrNotBlank;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserRole;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/09/09
 */
@Builder
public record UpdateUserRequest(
    @NullOrNotBlank(message = "validation.user.name.notBlank") @Size(min = 2, max = 50,
        message = "validation.user.name.size") String name,

    @NullOrNotBlank(message = "validation.user.role.notBlank") @Enum(enumClass = UserRole.class,
        message = "validation.user.role.enum", ignoreCase = true) String role)
    implements Serializable {

    public User toDomain(Long id)  {
        return User.builder()
            .id(id)
            .name(name)
            .role(role)
            .build();
    }
}
