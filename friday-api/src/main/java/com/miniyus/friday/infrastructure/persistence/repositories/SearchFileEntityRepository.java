package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.SearchFileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

interface SearchFileEntityRepository extends JpaRepository<SearchFileEntity, Long> {
}
