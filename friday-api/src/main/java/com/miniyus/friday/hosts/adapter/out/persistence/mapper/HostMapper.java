package com.miniyus.friday.hosts.adapter.out.persistence.mapper;

import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.hosts.domain.searches.Search;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HostMapper {
    public HostEntity create(Host domain, UserEntity userEntity) throws RestErrorException {
        return HostEntity.create(
            domain.getHostname(),
            domain.getSummary(),
            domain.getDescription(),
            domain.getPath(),
            domain.isPublish(),
            userEntity
        );
    }

    public HostEntity update(
        HostEntity entity,
        Host domain
    ) throws RestErrorException {
        return entity
            .setHost(domain.getHostname())
            .setSummary(domain.getSummary())
            .setDescription(domain.getDescription())
            .setPath(domain.getPath())
            .setPublish(domain.isPublish());
    }

    public Host toDomain(HostEntity entity, List<Search> searches) {
        return new Host(
            entity.getId(),
            entity.getHost(),
            entity.getSummary(),
            entity.getDescription(),
            entity.getPath(),
            entity.isPublish(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt(),
            entity.getUser().getId(),
            searches
        );
    }
}
