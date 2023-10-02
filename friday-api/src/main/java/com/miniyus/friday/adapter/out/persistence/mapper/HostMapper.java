package com.miniyus.friday.adapter.out.persistence.mapper;

import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.domain.hosts.Host;
import com.miniyus.friday.domain.hosts.searches.Search;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.entities.SearchEntity;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class HostMapper {
    public HostEntity create(Host domain, UserEntity userEntity) throws RestErrorException {
        return HostEntity.create(
            domain.getHost(),
            domain.getSummary(),
            domain.getDescription(),
            domain.getPath(),
            domain.isPublish(),
            userEntity
        );
    }

    public HostEntity toEntity(
        Host domain,
        UserEntity userEntity,
        List<SearchEntity> searchEntities
    ) throws RestErrorException {
        return new HostEntity(
            domain.getId(),
            domain.getHost(),
            domain.getSummary(),
            domain.getDescription(),
            domain.getPath(),
            domain.isPublish(),
            domain.getDeletedAt(),
            userEntity,
            searchEntities
        );
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
