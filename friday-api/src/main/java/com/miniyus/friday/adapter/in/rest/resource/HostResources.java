package com.miniyus.friday.adapter.in.rest.resource;

import com.miniyus.friday.domain.hosts.Host;
import lombok.Builder;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * DTO for {@link Host}
 */
public record HostResources(
    List<HostResource> hosts
) implements Serializable{
    @Builder
    public record HostResource(
        Long id,
        String host,
        String summary,
        String description,
        String path,
        boolean publish,
        LocalDateTime createdAt,
        LocalDateTime updatedAt)
        implements Serializable {
        public static HostResource fromDomain(Host host) {
            return HostResource.builder()
                .id(host.getId())
                .host(host.getHost())
                .summary(host.getSummary())
                .description(host.getDescription())
                .path(host.getPath())
                .publish(host.isPublish())
                .createdAt(host.getCreatedAt())
                .updatedAt(host.getUpdatedAt())
                .build();
        }
    }

    public static Page<HostResource> fromDomains(Page<Host> domains) {
        return domains.map(HostResource::fromDomain);
    }

}
