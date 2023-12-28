package com.miniyus.friday.hosts.domain.searches;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record PatchSearch(
    SearchIds ids,
    JsonNullable<String> queryKey,
    JsonNullable<String> query,
    JsonNullable<String> description,
    JsonNullable<Boolean> publish,
    JsonNullable<Long> imageId
) {
}
