package com.meteormin.friday.infrastructure.persistence.repositories;

import com.meteormin.friday.infrastructure.persistence.entities.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FileEntityRepository extends JpaRepository<FileEntity, Long> {
    List<FileEntity> findByIdIn(List<Long> ids);

    List<FileEntity> findByUserId(Long userId);
}
