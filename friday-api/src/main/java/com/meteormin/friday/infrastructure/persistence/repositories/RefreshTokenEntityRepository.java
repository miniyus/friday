package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

/**
 * Refresh Token Repository
 *
 * @author meteormin
 * @since 2023/09/02
 */
public interface RefreshTokenEntityRepository extends CrudRepository<RefreshTokenEntity, String> {
    /**
     * Finds a RefreshTokenEntity object by its token value.
     *
     * @param token the token value to search for
     * @return the RefreshTokenEntity object found, or null if not found
     */
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByAccessTokenId(String accessTokenId);
}
