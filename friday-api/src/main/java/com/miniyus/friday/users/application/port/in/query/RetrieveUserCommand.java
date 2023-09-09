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
    private String email;
    private String name;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private LocalDateTime updatedAtStart;
    private LocalDateTime updatedAtEnd;
    private Pageable pageable;
}
