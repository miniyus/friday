package com.miniyus.friday.hosts.adapter.out.persistence.mapper;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.hosts.domain.Host;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostMapper {
    private final UserEntityRepository userEntityRepository;

    public HostEntity create(Host domain) throws RestErrorException {
        var userEntity = userEntityRepository.findById(domain.getUserId())
            .orElseThrow(
                () -> new RestErrorException(RestErrorCode.NOT_FOUND, "user.error.notFound"));

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

    public Host toDomain(HostEntity entity) {
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
            entity.getUser().getId()
        );
    }
}
