package com.miniyus.friday.infrastructure.persistence.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE auth_user SET deleted_at = NOW() WHERE id = ?")
@Table(name = "auth_user")
@Getter
public class UserEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = true)
    private String snsId;

    @Column(nullable = true)
    private String provider;

    @Column(unique = true)
    private String email;

    private String password;

    private String name;

    private String role;

    @Column(nullable = true)
    private LocalDateTime deletedAt;

    /**
     * @param snsId
     * @param provider
     * @param email
     * @param password
     * @param name
     * @param role
     */
    @Builder
    public UserEntity(String snsId, String provider, String email, String password, String name,
            String role) {
        this.snsId = snsId;
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Updates the password, name, and role of the object.
     *
     * @param password the new password to be set
     * @param name the new name to be set
     * @param role the new role to be set
     */
    public void update(String password, String name, String role) {
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Deletes the object.
     *
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
