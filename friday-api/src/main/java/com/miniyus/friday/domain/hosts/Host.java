package com.miniyus.friday.domain.hosts;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
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

    @Builder
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

    @Builder
    public record HostFilter(
        String summary,
        String path,
        String description,
        LocalDateTime createdAtStart,
        LocalDateTime createdAtEnd,
        LocalDateTime updatedAtStart,
        LocalDateTime updatedAtEnd,
        Long userId
    ) {
        public boolean isEmpty() {
            return summary == null && path == null  && description == null
                && createdAtStart == null && updatedAtStart == null
                && createdAtEnd == null &&  updatedAtEnd == null;
        }
    }

    @Builder
    public record WherePublish(
        boolean publish,
        Long userId
    ){}

    @Builder
    public record WhereHost(
        String host,
        Long userId
    ){}
}
