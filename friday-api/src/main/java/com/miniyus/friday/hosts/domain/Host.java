package com.miniyus.friday.hosts.domain;


import com.miniyus.friday.hosts.domain.searches.Search;
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
    String hostname;
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

    public void patch(
        PatchHost patchHost
    ) {
        this.hostname = patchHost.host();
        this.summary = patchHost.summary();
        this.description = patchHost.description();
        this.path = patchHost.path();
        this.publish = patchHost.publish();
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
