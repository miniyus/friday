package com.miniyus.friday.infrastructure.jpa.repositories;

import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

/**
 * Access Token Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface AccessTokenRepository extends CrudRepository<AccessTokenEntity, String> {
    Optional<AccessTokenEntity> findByToken(String token);

    Optional<AccessTokenEntity> findByUserId(String userId);
}
