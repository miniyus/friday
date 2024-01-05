package com.meteormin.friday.hosts.domain.searches;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

import java.util.List;

@Builder
public record PatchSearch(
    SearchIds ids,
    JsonNullable<String> queryKey,
    JsonNullable<String> query,
    JsonNullable<String> description,
    JsonNullable<Boolean> publish,
    JsonNullable<List<Long>> images
) {
}
