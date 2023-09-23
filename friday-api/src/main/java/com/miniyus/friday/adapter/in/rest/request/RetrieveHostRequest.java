package com.miniyus.friday.adapter.in.rest.request;

import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.common.pagination.PageRequest;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;

/**
 * DTO for {@link Host}
 */
public class RetrieveHostRequest {

    @Builder
    @Getter
    public static class RetrieveAll extends PageRequest {
        @Nullable
        String host;
        @Nullable
        String summary;
        @Nullable
        String description;
        @Nullable
        String path;
        @Nullable
        LocalDateTime createdAtStart;
        @Nullable
        LocalDateTime createdAtEnd;
        @Nullable
        LocalDateTime updatedAtStart;
        @Nullable
        LocalDateTime updatedAtEnd;
        @Nullable
        Pageable pageable;

        public Pageable getPageable() {
            return toPageable();
        }
    }


    @Builder
    public record RetrieveHost(
        String host
    ) {
    }


    @Builder
    public record RetrievePublish(
        boolean publish
    ) {
    }
}
