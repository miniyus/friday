package com.miniyus.friday.domain.hosts.searches;

public record WhereSearch(
    Long hostId,
    String queryKey,
    String query
) {
}
