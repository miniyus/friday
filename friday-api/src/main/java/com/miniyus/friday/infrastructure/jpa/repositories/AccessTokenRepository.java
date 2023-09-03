package com.miniyus.friday.infrastructure.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface AccessTokenRepository extends JpaRepository<AccessTokenEntity, Long> {

}
