package com.miniyus.friday.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;
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
 * Search Entity
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

    /**
     * @param queryKey
     * @param query
     * @param description
     * @param publish
     */
    public SearchEntity(String queryKey, String query, String description, boolean publish) {
        this.queryKey = queryKey;
        this.query = query;
        this.description = description;
        this.publish = publish;
        this.views = 0;
    }

    /**
     * A description of the entire Java function.
     *
     * @param queryKey    a String representing the query key
     * @param query       a String representing the query
     * @param description a String representing the description
     * @param publish     a boolean indicating whether to publish
     */
    public void update(String queryKey, String query, String description, boolean publish) {
        this.queryKey = queryKey;
        this.query = query;
        this.description = description;
        this.publish = publish;
    }

    /**
     * Publishes the data.
     *
     * @param None No parameters are required.
     * @return None No return value.
     */
    public void publish() {
        this.publish = true;
    }

    /**
     * Unpublishes the item.
     *
     * @param none This function does not take any parameters.
     * @return This function does not return a value.
     */
    public void unpublish() {
        this.publish = false;
    }

    /**
     * Increments the value of the 'views' variable by 1.
     *
     * No parameters.
     * 
     * No return value.
     */
    public void incrementViews() {
        this.views++;
    }

    /**
     * Deletes the object by setting the deletedAt field to the current date and
     * time.
     *
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
