package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.AccessTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Access Token Repository
 *
 * @author meteormin
 * @since 2023/09/02
 */
public interface AccessTokenEntityRepository extends CrudRepository<AccessTokenEntity, String> {
    Optional<AccessTokenEntity> findByToken(String token);

    Optional<AccessTokenEntity> findByUserId(String userId);
}
