package com.meteormin.friday.api.searches.request;

import com.meteormin.friday.hosts.domain.searches.CreateSearch;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateSearchRequest(
    String queryKey,
    String query,
    String description,
    boolean publish,
    Long hostId,
    List<Long> images
) {

    public CreateSearch toDomain(Long hostId) {
        return CreateSearch.builder()
            .hostId(hostId)
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .images(images)
            .build();
    }
}
