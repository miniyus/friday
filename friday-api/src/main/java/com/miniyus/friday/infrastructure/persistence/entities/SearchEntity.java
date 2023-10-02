package com.miniyus.friday.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import java.util.List;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

/**
 * Search Entity
 *
 * @author miniyus
 * @date 2023/09/04
 */
@Getter
@Builder
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "search")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE search SET deleted_at = NOW() WHERE id = ?")
public class SearchEntity extends BaseEntity {

    @Id
    @GeneratedValue
    protected Long id;

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

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "file_id")
    private FileEntity file;

    @ManyToOne(cascade = CascadeType.ALL)
    private HostEntity host;

    /**
     * Instantiates a new Search entity.
     *
     * @param queryKey    the query key
     * @param query       the query
     * @param description the description
     * @param publish     the publishing status
     * @author miniyus
     * @date 2023/09/04
     */
    public static SearchEntity create(
        String queryKey,
        String query,
        String description,
        boolean publish,
        FileEntity file
    ) {
        return SearchEntity.builder()
            .queryKey(queryKey)
            .query(query)
            .description(description)
            .publish(publish)
            .views(0)
            .file(file)
            .build();
    }

    /**
     * A description of the entire Java function.
     *
     * @param queryKey    a String representing the query key
     * @param query       a String representing the query
     * @param description a String representing the description
     * @param publish     a boolean indicating whether to publish
     * @author miniyus
     * @date 2023/09/04
     */
    public void update(
        String queryKey,
        String query,
        String description,
        boolean publish,
        FileEntity file
    ) {
        this.queryKey = queryKey;
        this.query = query;
        this.description = description;
        this.publish = publish;
        this.file = file;
    }


    /**
     * Publish.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void publish() {
        this.publish = true;
    }


    /**
     * Unpublish.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void unpublish() {
        this.publish = false;
    }


    /**
     * Increment views.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void incrementViews() {
        this.views++;
    }


    /**
     * Delete.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
