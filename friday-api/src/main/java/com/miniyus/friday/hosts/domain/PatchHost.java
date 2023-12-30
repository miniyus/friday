package com.miniyus.friday.hosts.domain;

import lombok.Builder;
import org.openapitools.jackson.nullable.JsonNullable;

@Builder
public record PatchHost(
    HostIds ids,
    JsonNullable<String> host,
    JsonNullable<String> summary,
    JsonNullable<String> description,
    JsonNullable<String> path,
    JsonNullable<Boolean> publish
) {
}
