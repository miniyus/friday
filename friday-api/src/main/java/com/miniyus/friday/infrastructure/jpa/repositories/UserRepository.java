package com.miniyus.friday.infrastructure.jpa.repositories;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
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
     * @param snsId the social media ID of the user
     * @param provider the provider of the social media platform
     * @return an optional user entity that matches the given social media ID and provider
     */
    public Optional<UserEntity> findBySnsIdAndProvider(String snsId, String provider);

    @Query(value = "select u from UserEntity u" +
            "WHERE (u.email = :email OR :email IS NULL)" +
            "AND (u.name = :name OR :name IS NULL)" +
            "AND (:createdAtStart IS NULL OR :createdAtEnd IS NULL " +
            "OR u.createdAt BETWEEN :createdAtStart AND :createdAtEnd)" +
            "AND (:updatedAtStart IS NULL OR :updatedAtEnd IS NULL " +
            "OR u.updatedAt BETWEE :updatedAtStart AN :updatedAtEnd)", nativeQuery = true)
    public Page<UserEntity> findAll(
            @Param("email") String email,
            @Param("name") String name,
            @Param("createdAtStart") LocalDateTime createdAtStart,
            @Param("createdAtEnd") LocalDateTime createdAtEnd,
            @Param("updatedAtStart") LocalDateTime updatedAtStart,
            @Param("updatedAtEnd") LocalDateTime updatedAtEnd,
            Pageable pageable);

    /**
     * Checks if an email exists.
     * 
     * @param email the email to check
     * @return true if the email exists, false otherwise
     */
    public boolean existsByEmail(String email);
}
