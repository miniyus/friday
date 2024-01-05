package com.meteormin.friday.hosts.domain.searches;

import com.meteormin.friday.common.util.JsonNullableUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

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

    @Setter
    private String shortUrl;

    private LocalDateTime deletedAt;

    private Long hostId;

    private List<Long> files;

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
            .files(createSearch.images())
            .hostId(createSearch.hostId())
            .build();
    }

    public void incrementViews() {
        this.views++;
    }

    public void patch(
        PatchSearch updateSearch
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

        if (JsonNullableUtil.isPresent(updateSearch.images())) {
            this.files = JsonNullableUtil.unwrap(updateSearch.images(), null);
        }
    }
}
