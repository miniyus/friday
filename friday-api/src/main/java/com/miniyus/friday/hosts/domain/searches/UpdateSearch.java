package com.miniyus.friday.hosts.domain.searches;

public record UpdateSearch(
    SearchIds ids,
    String queryKey,
    String query,
    String description,
    boolean publish,
    SearchImage searchImage
) {
}
