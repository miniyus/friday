package com.miniyus.friday.infrastructure.jpa.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    public UserEntity findByEmail(String email);

    public UserEntity findBySnsIdAndProvider(String snsId, String provider);
}
