package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;
import java.util.Collection;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.jpa.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@SQLDelete(sql = "UPDATE user SET deleted_at = NOW() WHERE id = ?")
@Table(name = "tbl_user")
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

    @OneToMany(mappedBy = "user", fetch = FetchType.EAGER)
    private Collection<AccessTokenEntity> accessTokens;

    /**
     * @param snsId
     * @param provider
     * @param email
     * @param password
     * @param name
     * @param role
     */
    @Builder
    public UserEntity(String snsId, String provider, String email, String password, String name, String role) {
        this.snsId = snsId;
        this.provider = provider;
        this.email = email;
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Adds an access token to the list of access tokens.
     *
     * @param accessToken the access token to be added
     */
    public void addAccessToken(AccessTokenEntity accessToken) {
        this.accessTokens.add(accessToken);
    }

    /**
     * Updates the password, name, and role of the object.
     *
     * @param password the new password to be set
     * @param name     the new name to be set
     * @param role     the new role to be set
     */
    public void update(String password, String name, String role) {
        this.password = password;
        this.name = name;
        this.role = role;
    }

    /**
     * Deletes the object.
     *
     * @param None This function does not take any parameters.
     * @return This function does not return anything.
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
