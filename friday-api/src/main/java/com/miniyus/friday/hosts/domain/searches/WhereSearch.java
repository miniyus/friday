package com.miniyus.friday.hosts.domain.searches;

public record WhereSearch(
    Long hostId,
    String queryKey,
    String query
) {
}
