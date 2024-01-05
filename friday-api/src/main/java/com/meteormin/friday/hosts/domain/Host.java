package com.meteormin.friday.hosts.domain;


import com.meteormin.friday.common.util.JsonNullableUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

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
            createHost.userId()
        );
    }

    public void patch(
        PatchHost patchHost
    ) {
        if (JsonNullableUtil.isPresent(patchHost.host())) {
            this.hostname = JsonNullableUtil.unwrap(patchHost.host(), null);
        }

        if (JsonNullableUtil.isPresent(patchHost.summary())) {
            this.summary = JsonNullableUtil.unwrap(patchHost.summary(), null);
        }

        if (JsonNullableUtil.isPresent(patchHost.description())) {
            this.description = JsonNullableUtil.unwrap(patchHost.description(), null);
        }

        if (JsonNullableUtil.isPresent(patchHost.path())) {
            this.path = JsonNullableUtil.unwrap(patchHost.path(), null);
        }

        if (JsonNullableUtil.isPresent(patchHost.publish())) {
            this.publish = JsonNullableUtil.unwrap(patchHost.publish(), null);
        }
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
