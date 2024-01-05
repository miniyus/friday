package com.meteormin.friday.users.domain;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record PatchUser(
    Long id,
    JsonNullable<String> name,
    JsonNullable<UserRole> role
) {
}
