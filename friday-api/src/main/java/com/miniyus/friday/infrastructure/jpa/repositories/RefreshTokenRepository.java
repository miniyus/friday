package com.miniyus.friday.infrastructure.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;

import java.util.Optional;

/**
 * Refresh Token Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    /**
     * Finds a RefreshTokenEntity object by its token value.
     *
     * @param token the token value to search for
     * @return the RefreshTokenEntity object found, or null if not found
     */
    Optional<RefreshTokenEntity> findByToken(String token);
}
