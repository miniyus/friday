package com.miniyus.friday.infrastructure.persistence.entities;

import com.miniyus.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * Host Entity
 *
 * @author miniyus
 * @date 2023/09/04
 */
@Entity
@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@Table(name = "host")
@Where(clause = "deleted_at is null")
@SQLDelete(sql = "UPDATE host SET deleted_at = NOW() WHERE id = ?")
public class HostEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    protected Long id;

    /**
     * The Host.
     */
    private String host;

    /**
     * The Summary.
     */
    private String summary;

    /**
     * The Description.
     */
    private String description;

    /**
     * The Path.
     */
    private String path;

    /**
     * The Publishing status
     */
    private boolean publish;

    /**
     * The Deleted at.
     */
    @Column(nullable = true)
    private LocalDateTime deletedAt;

    /**
     * The User.
     */
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private UserEntity user;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "host")
    private List<SearchEntity> searches;

    /**
     * Instantiates a new Host entity.
     *
     * @param host        the host
     * @param summary     the summary
     * @param description the description
     * @param path        the path
     * @param publish     the publishing status
     * @author miniyus
     * @date 2023/09/04
     */
    public static HostEntity create(
        String host,
        String summary,
        String description,
        String path,
        boolean publish,
        UserEntity user
    ) {
        return HostEntity.builder()
            .host(host)
            .summary(summary)
            .description(description)
            .path(path)
            .publish(publish)
            .user(user)
            .build();
    }

    /**
     * Updates the host, summary, description, path, and publish status.
     *
     * @param host        The new host value.
     * @param summary     The new summary value.
     * @param description The new description value.
     * @param path        The new path value.
     * @param publish     The new publishing value.
     * @author miniyus
     * @date 2023/09/04
     */
    public void update(String host, String summary, String description, String path,
        boolean publish) {
        this.host = host;
        this.summary = summary;
        this.description = description;
        this.path = path;
        this.publish = publish;
    }

    /**
     * Publishes the data.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void publish() {
        this.publish = true;
    }

    /**
     * Unpublishes the item by setting the 'publish' flag to false.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void unpublish() {
        this.publish = false;
    }

    /**
     * Deletes the record by setting the `deletedAt` field to the current date and time.
     *
     * @author miniyus
     * @date 2023/09/04
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    public void createSearch(SearchEntity searchEntity) {
        this.searches.add(searchEntity);
    }

    public void deleteSearch(Long id) {
        var filtered = this.searches
            .stream()
            .filter(s -> s.getId().equals(id))
            .toList();

        this.searches.remove(filtered.get(0));
    }

    public Optional<SearchEntity> findSearch(Long id) {
        return this.searches
            .stream()
            .filter(s -> s.getId().equals(id))
            .findFirst();
    }
}
