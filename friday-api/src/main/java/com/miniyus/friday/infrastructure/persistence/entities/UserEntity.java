package com.miniyus.friday.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import com.miniyus.friday.infrastructure.security.UserRole;
import jakarta.persistence.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;
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

    @Column
    private String snsId;

    @Column
    private String provider;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column
    private LocalDateTime deletedAt;

    /**
     * @param snsId    sns id
     * @param provider provider
     * @param email    email
     * @param password password
     * @param name     name
     * @param role     role
     */
    @Builder
    public UserEntity(
        String snsId,
        String provider,
        String email,
        String password,
        String name,
        UserRole role) {
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
     * @param name     the new name to be set
     * @param role     the new role to be set
     */
    public void update(String password, String name, UserRole role) {
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Deletes the object.
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
