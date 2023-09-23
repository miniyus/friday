package com.miniyus.friday.domain.hosts;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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

    public static Host create(
        String host,
        String summary,
        String description,
        String path,
        boolean publish,
        Long userId
    ) {
        return new Host(
            null,
            host,
            summary,
            description,
            path,
            publish,
            null,
            null,
            null,
            userId
        );
    }

    public void update(
        String host,
        String summary,
        String description,
        String path,
        boolean publish
    ) {
        this.host = host;
        this.summary = summary;
        this.description = description;
        this.path = path;
        this.publish = publish;
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
