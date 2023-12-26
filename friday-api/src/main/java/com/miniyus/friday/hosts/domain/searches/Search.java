package com.miniyus.friday.hosts.domain.searches;

import com.miniyus.friday.common.util.JsonNullableUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class Search {
    private Long id;
    private String queryKey;
    private String query;
    private String description;
    private boolean publish;
    private int views;
    private String shortUrl;
    private Long imageId;
    private LocalDateTime deletedAt;
    private Long hostId;
    private Long fileId;

    public static Search create(
        CreateSearch createSearch
    ) {
        return Search.builder()
            .queryKey(createSearch.queryKey())
            .query(createSearch.query())
            .description(createSearch.description())
            .publish(createSearch.publish())
            .views(0)
            .shortUrl(null)
            .imageId(createSearch.imageId())
            .hostId(createSearch.hostId())
            .build();
    }

    public void incrementViews() {
        this.views++;
    }

    public void setShortUrl(String shortUrl) {
        this.shortUrl = shortUrl;
    }

    public void update(
        UpdateSearch updateSearch
    ) {
        if (JsonNullableUtil.isPresent(updateSearch.queryKey())) {
            this.queryKey = JsonNullableUtil.unwrap(updateSearch.queryKey(), null);
        }

        if (JsonNullableUtil.isPresent(updateSearch.query())) {
            this.query = JsonNullableUtil.unwrap(updateSearch.query(), null);
        }

        if (JsonNullableUtil.isPresent(updateSearch.description())) {
            this.description = JsonNullableUtil.unwrap(updateSearch.description(), null);
        }

        if (JsonNullableUtil.isPresent(updateSearch.publish())) {
            this.publish = JsonNullableUtil.unwrap(updateSearch.publish(), false);
        }

        if (JsonNullableUtil.isPresent(updateSearch.imageId())) {
            this.fileId = JsonNullableUtil.unwrap(updateSearch.imageId(), null);
        }

        if (JsonNullableUtil.isPresent(updateSearch.imageId())) {
            this.imageId = JsonNullableUtil.unwrap(updateSearch.imageId(), null);
        }
    }
}
