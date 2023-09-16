package com.miniyus.friday.infrastructure.jpa.repositories;

import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;
import org.springframework.data.repository.CrudRepository;
import java.util.Optional;

/**
 * Refresh Token Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface RefreshTokenRepository extends CrudRepository<RefreshTokenEntity, String> {
    /**
     * Finds a RefreshTokenEntity object by its token value.
     *
     * @param token the token value to search for
     * @return the RefreshTokenEntity object found, or null if not found
     */
    Optional<RefreshTokenEntity> findByToken(String token);

    Optional<RefreshTokenEntity> findByAccessTokenId(String accessTokenId);
}
