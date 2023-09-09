package com.miniyus.friday.users.adapter.in.rest.request;

import java.time.LocalDateTime;
import org.springframework.data.domain.Pageable;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RetrieveUserRequest {
    private String email;
    private String name;
    private LocalDateTime createdAtStart;
    private LocalDateTime createdAtEnd;
    private LocalDateTime updatedAtStart;
    private LocalDateTime updatedAtEnd;

    public RetrieveUserCommand toCommand(Pageable pageable) {
        return RetrieveUserCommand.builder()
                .email(email)
                .name(name)
                .createdAtStart(createdAtStart)
                .createdAtEnd(createdAtEnd)
                .updatedAtStart(updatedAtStart)
                .updatedAtEnd(updatedAtEnd)
                .pageable(pageable)
                .build();
    }
}
