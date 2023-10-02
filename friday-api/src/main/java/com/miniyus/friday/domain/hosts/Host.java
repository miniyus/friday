package com.miniyus.friday.domain.hosts;


import com.miniyus.friday.domain.hosts.searches.Search;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class Host {
    Long id;
    String host;
    String summary;
    String description;
    String path;
    boolean publish;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;
    LocalDateTime deletedAt;
    Long userId;
    List<Search> searches;

    public static Host create(
        CreateHost createHost
    ) {
        return new Host(
            null,
            createHost.host(),
            createHost.summary(),
            createHost.description(),
            createHost.path(),
            createHost.publish(),
            null,
            null,
            null,
            createHost.userId(),
            null
        );
    }

    public void update(
        UpdateHost updateHost
    ) {
        this.host = updateHost.host();
        this.summary = updateHost.summary();
        this.description = updateHost.description();
        this.path = updateHost.path();
        this.publish = updateHost.publish();
    }

    public void enablePublish() {
        this.publish = true;
    }

    public void disablePublish() {
        this.publish = false;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
