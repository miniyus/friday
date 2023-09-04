package com.miniyus.friday.infrastructure.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    RefreshTokenEntity findByToken(String token);
}
