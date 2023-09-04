package com.miniyus.friday.infrastructure.jpa.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;

/**
 * User Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Finds a user entity by email.
     * 
     * @param email the email of the user to find
     * @return an optional containing the found user entity, or empty if not found
     */
    public Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a user entity by social media ID and provider.
     * 
     * @param snsId    the social media ID of the user
     * @param provider the provider of the social media platform
     * @return an optional user entity that matches the given social media ID and
     *         provider
     */
    public Optional<UserEntity> findBySnsIdAndProvider(String snsId, String provider);

}
