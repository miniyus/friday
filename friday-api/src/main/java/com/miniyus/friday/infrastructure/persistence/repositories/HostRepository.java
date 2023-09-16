package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.HostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDateTime;
import java.util.Optional;

public interface HostRepository extends JpaRepository<HostEntity, Long> {
    boolean existsByHostAndUserId(String host, Long userId);

    Optional<HostEntity> findByHostAndUserId(String host, Long userId);

    Page<HostEntity> findByPublishAndUserId(boolean publish, Long userId, Pageable pageable);

    @Query(value = "select h from host h "
        + "WHERE (h.summary = :summary OR :summary IS NULL) "
        + "AND (h.description = :description OR :description IS NULL) "
        + "AND (h.path = :path OR :path IS NULL) "
        + "AND (h.userId = :userId OR :userId IS NULL) "
        + "AND (:createdAtStart IS NULL OR :createdAtEnd IS NULL "
        + "OR h.createdAt BETWEEN :createdAtStart AND :createdAtEnd) "
        + "AND (:updatedAtStart IS NULL OR :updatedAtEnd IS NULL "
        + "OR h.createdAt BETWEEN :updatedAtStart AND :updatedAtEnd)",
        nativeQuery = true
    )
    Page<HostEntity> findAll(
        @Param("summary") String summary,
        @Param("description") String description,
        @Param("path") String path,
        @Param("userId") Long userId,
        @Param("createdAtStart") LocalDateTime createdAtStart,
        @Param("createdAtEnd") LocalDateTime createdAtEnd,
        @Param("updatedAtStart") LocalDateTime updatedAtStart,
        @Param("updatedAtEnd") LocalDateTime updatedAtEnd,
        Pageable pageable
    );
}
