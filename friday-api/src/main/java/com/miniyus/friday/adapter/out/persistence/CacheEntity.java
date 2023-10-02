package com.miniyus.friday.adapter.out.persistence;

import com.miniyus.friday.infrastructure.persistence.BaseEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public abstract class CacheEntity<T extends BaseEntity> {
    private T cacheEntity;

    protected abstract JpaRepository<T, Long> getCacheRepository();

    protected Optional<T> getCacheEntity(Long id) {
        if (cacheEntity == null || !cacheEntity.getId().equals(id)) {
            cacheEntity = getCacheRepository().findById(id).orElse(null);
        }

        return Optional.ofNullable(cacheEntity);
    }
}
