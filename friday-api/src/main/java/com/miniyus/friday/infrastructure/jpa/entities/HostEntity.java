package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@Entity
@Getter
@Where(clause = "deleted_at is null")
@Table(name = "host")
@AllArgsConstructor
@NoArgsConstructor
public class HostEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String host;

    private String summary;

    private String description;

    private String path;

    private boolean publish;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserEntity user;
}
