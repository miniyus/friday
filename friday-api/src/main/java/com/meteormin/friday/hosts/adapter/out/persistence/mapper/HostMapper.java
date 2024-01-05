package com.meteormin.friday.hosts.adapter.out.persistence.mapper;

import com.meteormin.friday.common.error.RestErrorCode;
import com.meteormin.friday.common.error.RestErrorException;
import com.meteormin.friday.hosts.domain.Host;
import com.meteormin.friday.infrastructure.persistence.entities.HostEntity;
import com.meteormin.friday.infrastructure.persistence.repositories.UserEntityRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HostMapper {
    private final UserEntityRepository userEntityRepository;

    public HostEntity createHostEntity(Host domain) throws RestErrorException {
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

    public HostEntity updateHostEntity(
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

    public Host toHostDomain(HostEntity entity) {
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
