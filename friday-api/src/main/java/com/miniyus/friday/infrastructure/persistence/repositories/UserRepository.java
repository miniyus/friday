package com.miniyus.friday.infrastructure.persistence.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

/**
 * User Repository
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface UserRepository extends JpaRepository<UserEntity, Long>, QUserRepository {
    /**
     * Finds a user entity by email.
     *
     * @param email the email of the user to find
     * @return an optional containing the found user entity, or empty if not found
     */
    Optional<UserEntity> findByEmail(String email);

    /**
     * Finds a user entity by social media ID and provider.
     *
     * @param snsId the social media ID of the user
     * @param provider the provider of the social media platform
     * @return an optional user entity that matches the given social media ID and provider
     */
    Optional<UserEntity> findBySnsIdAndProvider(String snsId, String provider);

    /**
     * Checks if an email exists.
     *
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    boolean existsByEmail(String email);

}
