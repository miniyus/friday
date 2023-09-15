package com.miniyus.friday.api.users.application.port.in.query;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import lombok.Builder;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Builder
public record RetrieveUserCommand(
        String email,
        String name,
        LocalDateTime createdAtStart,
        LocalDateTime createdAtEnd,
        LocalDateTime updatedAtStart,
        LocalDateTime updatedAtEnd,
        Pageable pageable) {
}
