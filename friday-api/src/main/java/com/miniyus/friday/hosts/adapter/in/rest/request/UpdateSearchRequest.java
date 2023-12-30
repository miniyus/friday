package com.miniyus.friday.hosts.adapter.in.rest.request;

import com.miniyus.friday.hosts.domain.searches.PatchSearch;
import com.miniyus.friday.hosts.domain.searches.SearchIds;
import io.swagger.v3.oas.annotations.media.Schema;
import org.openapitools.jackson.nullable.JsonNullable;

public record UpdateSearchRequest(
    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> queryKey,

    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> query,

    @Schema(type = "string", requiredMode = Schema.RequiredMode.REQUIRED)
    JsonNullable<String> description,

    @Schema(type = "boolean", requiredMode = Schema.RequiredMode.REQUIRED)
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
