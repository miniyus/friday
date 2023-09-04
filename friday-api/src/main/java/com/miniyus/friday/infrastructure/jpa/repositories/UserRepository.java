package com.miniyus.friday.infrastructure.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public Optional<UserEntity> findByEmail(String email);

    public Optional<UserEntity> findBySnsIdAndProvider(String snsId, String provider);
}
