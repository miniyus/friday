package com.miniyus.friday.api.hosts.adapter.out.persistence;

import com.miniyus.friday.api.hosts.domain.Host;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class HostMapper {
    private final UserRepository userRepository;

    private UserEntity cacheUser;

    private UserEntity getUser(Long userId) {
        if (cacheUser == null) {
            cacheUser = userRepository.findById(userId)
                .orElseThrow(
                    () -> new RestErrorException("error.userNotFound", RestErrorCode.NOT_FOUND)
                );
        } else if (!cacheUser.getId().equals(userId)) {
            cacheUser = userRepository.findById(userId)
                .orElseThrow(
                    () -> new RestErrorException("error.userNotFound", RestErrorCode.NOT_FOUND)
                );
        }

        return cacheUser;
    }

    public HostEntity toEntity(Host domain) throws RestErrorException {
        var user = getUser(domain.getUserId());
        return new HostEntity(
            domain.getId(),
            domain.getHost(),
            domain.getSummary(),
            domain.getDescription(),
            domain.getPath(),
            domain.isPublish(),
            domain.getDeletedAt(),
            user
        );
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
