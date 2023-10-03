package com.miniyus.friday.infrastructure.persistence;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

/**
 * Base Entity
 * 
 * auto fill createdAt, updatedAt
 * 
 * @author seongminyoo
 * @date 2023/09/04
 */
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@MappedSuperclass
public abstract class BaseEntity<T> {

    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    public abstract T getId();
}
