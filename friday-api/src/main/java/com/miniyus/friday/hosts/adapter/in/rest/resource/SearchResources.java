package com.miniyus.friday.hosts.adapter.in.rest.resource;

import com.miniyus.friday.hosts.domain.searches.Search;
import lombok.Builder;

import java.util.List;

public record SearchResources(
    List<SearchResource> searches
) {
    @Builder
    public record SearchResource(
        Long id,
        String queryKey,
        String query,
        String description,
        boolean publish,
        int views,
        String shortUrl,
        Long hostId,
        String imageUrl
    ) {
        public static SearchResource fromDomain(Search search) {
            return SearchResource.builder()
                .id(search.getId())
                .queryKey(search.getQueryKey())
                .query(search.getQuery())
                .description(search.getDescription())
                .publish(search.isPublish())
                .views(search.getViews())
                .shortUrl(search.getShortUrl())
                .hostId(search.getHostId())
                .imageUrl(search.getShortUrl())
                .build();
        }
    }
}
