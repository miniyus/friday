package com.miniyus.friday.api.users.application.port.in.usecase;

import lombok.Builder;

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
        String role) {
}
