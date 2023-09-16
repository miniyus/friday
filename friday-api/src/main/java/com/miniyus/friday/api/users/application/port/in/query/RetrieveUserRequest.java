package com.miniyus.friday.api.users.application.port.in.query;

import java.io.Serializable;
import java.time.LocalDateTime;
import com.miniyus.friday.common.pagination.PageRequest;
import jakarta.annotation.Nullable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Getter
@AllArgsConstructor
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
}
