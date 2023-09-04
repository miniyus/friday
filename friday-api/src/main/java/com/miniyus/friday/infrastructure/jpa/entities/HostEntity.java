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
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Host Entity
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

    /**
     * @param host
     * @param summary
     * @param description
     * @param path
     * @param publish
     */
    @Builder
    public HostEntity(String host, String summary, String description, String path, boolean publish) {
        this.host = host;
        this.summary = summary;
        this.description = description;
        this.path = path;
        this.publish = publish;
    }

    /**
     * Updates the host, summary, description, path, and publish status.
     * 
     * @param host        The new host value.
     * @param summary     The new summary value.
     * @param description The new description value.
     * @param path        The new path value.
     * @param publish     The new publish value.
     */
    public void update(String host, String summary, String description, String path, boolean publish) {
        this.host = host;
        this.summary = summary;
        this.description = description;
        this.path = path;
        this.publish = publish;
    }

    /**
     * Publishes the data.
     *
     * @param None This function does not accept any parameters.
     * @return This function does not return any value.
     */
    public void publish() {
        this.publish = true;
    }

    /**
     * Unpublishes the item by setting the 'publish' flag to false.
     */
    public void unpublish() {
        this.publish = false;
    }

    /**
     * Deletes the record by setting the `deletedAt` field to the current date and
     * time.
     *
     * @param None
     * @return void
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
