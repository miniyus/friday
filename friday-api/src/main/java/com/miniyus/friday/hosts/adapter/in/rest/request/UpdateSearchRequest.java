package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.domain.searches.PatchSearch;
import com.miniyus.friday.hosts.domain.searches.SearchIds;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateSearchRequest(
    JsonNullable<String> queryKey,
    JsonNullable<String> query,
    JsonNullable<String> description,
    JsonNullable<Boolean> publish
) {

    public PatchSearch toDomain(SearchIds ids) {
        return PatchSearch.builder()
            .ids(ids)
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .build();
    }
}
