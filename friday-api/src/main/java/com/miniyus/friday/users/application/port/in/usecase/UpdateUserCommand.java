package com.miniyus.friday.users.application.port.in.usecase;

import lombok.Builder;
import lombok.Value;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
@Builder
public record UpdateUserCommand(
    Long id,
    String name,
    String role
) {
}
