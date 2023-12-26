package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.domain.searches.SearchIds;
import com.miniyus.friday.hosts.domain.searches.UpdateSearch;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateSearchRequest(
    JsonNullable<String> queryKey,
    JsonNullable<String> query,
    JsonNullable<String> description,
    JsonNullable<Boolean> publish
) {

    public UpdateSearch toDomain(SearchIds ids) {
        return UpdateSearch.builder()
            .ids(ids)
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .build();
    }
}
