package com.meteormin.friday.api.searches.request;


import com.meteormin.friday.hosts.domain.searches.SearchFilter;
import jakarta.annotation.Nullable;
import org.springframework.data.domain.Pageable;

public record RetrieveSearchRequest(
    @Nullable
    String queryKey,
    @Nullable
    String query,
    @Nullable
    Boolean publish
) {
    public SearchFilter toDomain(Long userId, Long hostId, Pageable pageable) {
        return SearchFilter.builder()
            .userId(userId)
            .hostId(hostId)
            .queryKey(queryKey)
            .query(query)
            .publish(publish)
            .pageable(pageable)
            .build();
    }
}
