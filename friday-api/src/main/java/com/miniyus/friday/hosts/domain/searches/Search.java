package com.miniyus.friday.hosts.domain.searches;

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
    private SearchImage searchImage;
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
            .searchImage(createSearch.searchImage())
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
        this.queryKey = updateSearch.queryKey();
        this.query = updateSearch.query();
        this.description = updateSearch.description();
        this.publish = updateSearch.publish();
        this.searchImage = updateSearch.searchImage();
    }
}
