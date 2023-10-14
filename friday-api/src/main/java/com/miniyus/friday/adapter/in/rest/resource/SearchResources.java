package com.miniyus.friday.adapter.in.rest.resource;

import java.util.List;

public record SearchResources(
    List<SearchResource> searches
) {
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
    }
}
