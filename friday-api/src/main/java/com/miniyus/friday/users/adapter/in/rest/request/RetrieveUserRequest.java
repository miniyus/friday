package com.miniyus.friday.users.adapter.in.rest.request;

import java.time.LocalDateTime;

import com.miniyus.friday.common.pagination.PageRequest;
import org.springframework.data.domain.Pageable;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserCommand;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

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
public class RetrieveUserRequest extends PageRequest{
    @Nullable
    private String email;

    @Nullable
    private String name;

    @Nullable
    private LocalDateTime createdAtStart;

    @Nullable
    private LocalDateTime createdAtEnd;

    @Nullable
    private LocalDateTime updatedAtStart;

    @Nullable
    private LocalDateTime updatedAtEnd;

    public RetrieveUserCommand toCommand() {
        return RetrieveUserCommand.builder()
                .email(email)
                .name(name)
                .createdAtStart(createdAtStart)
                .createdAtEnd(createdAtEnd)
                .updatedAtStart(updatedAtStart)
                .updatedAtEnd(updatedAtEnd)
                .pageable(toPageable())
                .build();
    }
}
