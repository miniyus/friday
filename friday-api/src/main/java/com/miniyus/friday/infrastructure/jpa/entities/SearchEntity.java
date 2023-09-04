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
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at is null")
@Table(name = "search")
public class SearchEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private String queryKey;

    private String query;

    private String description;

    private boolean publish;

    private int views;

    private LocalDateTime deletedAt;

    @Column(nullable = true)
    private String shortUrl;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private HostEntity host;
}
