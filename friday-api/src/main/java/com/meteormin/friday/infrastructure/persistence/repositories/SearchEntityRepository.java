package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.SearchEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SearchEntityRepository
    extends JpaRepository<SearchEntity, Long>, QSearchEntityRepository {
    Optional<SearchEntity> findByIdAndHostId(Long id, Long hostId);

    boolean existsByHostIdAndQueryKeyAndQuery(Long hostId, String queryKey, String query);
}
