package com.miniyus.friday.hosts.adapter.out.persistence;

import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.context.SecurityContextHolder;

@PersistenceAdapter
@RequiredArgsConstructor
public class OwnerAdapter extends CacheEntity<UserEntity> {
    private final UserEntityRepository repository;

    public UserEntity getUserEntity() {
        var principal = userInfo();
        if (principal == null) {
            throw accessDeniedException();
        } else if (principal.getId() == null) {
            throw accessDeniedException();
        }

        return getCacheEntity(principal.getId())
            .orElseThrow(this::accessDeniedException);
    }

    @Override
    protected JpaRepository<UserEntity, Long> getCacheRepository() {
        return repository;
    }

    private AccessDeniedException accessDeniedException() {
        return new AccessDeniedException("error.accessDenied");
    }

    private PrincipalUserInfo userInfo() {
        return (PrincipalUserInfo) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
    }

}
