package com.miniyus.friday.infrastructure.persistence.entities;

import java.time.LocalDateTime;

import com.miniyus.friday.common.UserRole;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;

/**
 * User Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Entity
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "auth_user")
@Where(clause = "deleted_at IS NULL")
@SQLDelete(sql = "UPDATE auth_user SET deleted_at = NOW() WHERE id = ?")
public class UserEntity extends BaseEntity<Long> {

    @Id
    @GeneratedValue
    protected Long id;

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
     * @author miniyus
     * @date 2023/09/02
     */
    public static UserEntity create(
        String snsId,
        String provider,
        String email,
        String password,
        String name,
        UserRole role) {
        return UserEntity.builder()
            .snsId(snsId)
            .provider(provider)
            .email(email)
            .password(password)
            .name(name)
            .role(role)
            .build();
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

    public void resetPassword(String password) {
        this.password = password;
    }
}
