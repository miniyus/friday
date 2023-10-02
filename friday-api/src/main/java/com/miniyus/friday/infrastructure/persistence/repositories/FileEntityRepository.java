package com.miniyus.friday.infrastructure.persistence.repositories;

import com.miniyus.friday.infrastructure.persistence.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
}
