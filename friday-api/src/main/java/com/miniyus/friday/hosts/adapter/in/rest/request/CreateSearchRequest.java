package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.domain.searches.CreateSearch;
import lombok.Builder;

@Builder
public record CreateSearchRequest(
    String queryKey,
    String query,
    String description,
    boolean publish,
    Long hostId,
    Long imageId
) {

    public CreateSearch toDomain(Long hostId) {
        return CreateSearch.builder()
            .hostId(hostId)
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .imageId(imageId)
            .build();
    }
}
