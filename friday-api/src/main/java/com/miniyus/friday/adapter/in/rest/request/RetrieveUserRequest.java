package com.miniyus.friday.adapter.in.rest.request;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.miniyus.friday.common.pagination.PageRequest;
import com.miniyus.friday.domain.users.UserFilter;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Getter
@Builder
public class RetrieveUserRequest extends PageRequest implements Serializable {
    @Nullable
    final String email;

    @Nullable
    final String name;

    @Nullable
    final LocalDateTime createdAtStart;

    @Nullable
    final LocalDateTime createdAtEnd;

    @Nullable
    final LocalDateTime updatedAtStart;

    @Nullable
    final LocalDateTime updatedAtEnd;

    @Nullable
    final Pageable pageable;

    public Pageable getPageable() {
        return toPageable();
    }

    public UserFilter toDomain() {
        return UserFilter.builder()
            .email(email)
            .name(name)
            .createdAtStart(createdAtStart)
            .createdAtEnd(createdAtEnd)
            .updatedAtStart(updatedAtStart)
            .updatedAtEnd(updatedAtEnd)
            .build();
    }

    public static RetrieveUserRequest create() {
        return RetrieveUserRequest.builder()
            .pageable(
                org.springframework.data.domain.PageRequest.of(
                    0,
                    20,
                    Sort.Direction.DESC,
                    "createdAt")
            ).build();
    }

    public static RetrieveUserRequest create(
        int page,
        int pageSize,
        String sortDirection,
        String sortField
    ) {
        return RetrieveUserRequest.builder()
            .pageable(
                org.springframework.data.domain.PageRequest.of(
                    page,
                    pageSize,
                    Sort.Direction.valueOf(sortDirection),
                    sortField)
            ).build();
    }
}
