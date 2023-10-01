package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

/**
 * Access Token Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface AccessTokenEntityRepository extends CrudRepository<AccessTokenEntity, String> {
    Optional<AccessTokenEntity> findByToken(String token);

    Optional<AccessTokenEntity> findByUserId(String userId);
}
