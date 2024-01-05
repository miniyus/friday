package com.meteormin.friday.api.users.request;

import com.meteormin.friday.common.util.JsonNullableUtil;
import com.meteormin.friday.common.validation.annotation.Enum;
import com.meteormin.friday.common.validation.annotation.NullOrNotBlank;
import com.meteormin.friday.users.domain.PatchUser;
import com.meteormin.friday.users.domain.UserRole;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

import java.io.Serializable;

/**
 * Update user request
 *
 * @author meteormin
 * @since 2023/09/09
 */
@Builder
public record UpdateUserRequest(
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    @NotBlank(message = "validation.user.name.notBlank") @Size(min = 2, max = 50,
        message = "validation.user.name.size")
    JsonNullable<String> name,

    @Schema(type = "string", requiredMode = Schema.RequiredMode.NOT_REQUIRED)
    @NullOrNotBlank(message = "validation.user.role.notBlank")
    @Enum(enumClass = UserRole.class,
        message = "validation.user.role.enum", ignoreCase = true)
    JsonNullable<String> role)
    implements Serializable {

    public PatchUser toDomain(Long id) {
        var builder = PatchUser.builder()
            .id(id)
            .name(name);
        if (JsonNullableUtil.isPresent(role)) {
            var userRole = JsonNullableUtil.unwrap(role, null);
            if (userRole == null) {
                builder.role(null);
            } else {
                builder.role(JsonNullable.of(
                    UserRole.of(userRole, true)));
            }
        }

        return builder.build();
    }
}
