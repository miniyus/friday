package com.miniyus.friday.users.application.port.in.query;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import lombok.Builder;
import lombok.Value;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Value
@Builder
public class RetrieveUserCommand {
    String email;
    String name;
    LocalDateTime createdAtStart;
    LocalDateTime createdAtEnd;
    LocalDateTime updatedAtStart;
    LocalDateTime updatedAtEnd;
    Pageable pageable;
}
