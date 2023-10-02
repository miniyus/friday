package com.miniyus.friday.adapter.out.persistence;

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
public class OwnerAdapter extends CacheEntity<UserEntity>{
    private final UserEntityRepository repository;

    private PrincipalUserInfo userInfo() {
        return (PrincipalUserInfo) SecurityContextHolder.getContext()
            .getAuthentication()
            .getPrincipal();
    }

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

    private AccessDeniedException accessDeniedException() {
        return new AccessDeniedException("error.accessDenied");
    }

    @Override
    protected JpaRepository<UserEntity, Long> getCacheRepository() {
        return repository;
    }
}
