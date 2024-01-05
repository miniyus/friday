package com.meteormin.friday.infrastructure.persistence.entities;

import com.meteormin.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Search Entity
 *
 * @author meteormin
 * @since 2023/09/04
 */
@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "search")
@SQLRestriction("deleted_at is null")
@SQLDelete(sql = "UPDATE search SET deleted_at = NOW() WHERE id = ?")
public class SearchEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    private Long id;

    /**
     * The Query key.
     */
    @Column
    @NonNull
    private String queryKey;

    /**
     * The Query.
     */
    @Column(nullable = false)
    @NonNull
    private String query;

    /**
     * The Description.
     */
    @Column(nullable = false)
    @NonNull
    private String description;

    /**
     * The Publishing status.
     */
    @Column(nullable = false)
    private boolean publish;

    /**
     * The Views.
     */
    @Column(nullable = false)
    private int views;

    /**
     * The Deleted at.
     */
    @Column
    @Nullable
    private LocalDateTime deletedAt;

    /**
     * The Short url.
     */
    @Column
    @Nullable
    private String shortUrl;

    @OneToMany(cascade = CascadeType.ALL,
        orphanRemoval = true,
        mappedBy = "search")
    private List<SearchFileEntity> searchFiles;

    @ManyToOne
    private HostEntity host;

    /**
     * Instantiates a new Search entity.
     *
     * @param queryKey    the query key
     * @param query       the query
     * @param description the description
     * @param publish     the publishing status
     * @author meteormin
     * @since 2023/09/04
     */
    public static SearchEntity create(
        String queryKey,
        String query,
        String description,
        boolean publish
    ) {
        return SearchEntity.builder()
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .views(0)
            .build();
    }

    /**
     * A description of the entire Java function.
     *
     * @param queryKey    a String representing the query key
     * @param query       a String representing the query
     * @param description a String representing the description
     * @param publish     a boolean indicating whether to publish
     * @author meteormin
     * @since 2023/09/04
     */
    public void update(
        String queryKey,
        String query,
        String description,
        boolean publish
    ) {
        this.queryKey = queryKey;
        this.query = query;
        this.description = description;
        this.publish = publish;
    }


    /**
     * Publish.
     *
     * @author meteormin
     * @since 2023/09/04
     */
    public void publish() {
        this.publish = true;
    }


    /**
     * Unpublish.
     *
     * @author meteormin
     * @since 2023/09/04
     */
    public void unpublish() {
        this.publish = false;
    }


    /**
     * Increment views.
     *
     * @author meteormin
     * @since 2023/09/04
     */
    public void incrementViews() {
        this.views++;
    }


    /**
     * Delete.
     *
     * @author meteormin
     * @since 2023/09/04
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
