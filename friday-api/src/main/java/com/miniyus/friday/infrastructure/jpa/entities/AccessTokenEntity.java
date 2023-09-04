package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.jpa.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
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
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Where(clause = "revoked = false")
@Table(name = "access_token")
public class AccessTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String token;

    private LocalDateTime expiresAt;

    private boolean revoked;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private UserEntity user;

    @OneToOne(fetch = FetchType.EAGER, mappedBy = "accessToken", cascade = CascadeType.ALL)
    private RefreshTokenEntity refreshToken;

    /**
     * @param type
     * @param token
     * @param expiresAt
     * @param user
     */
    @Builder
    public AccessTokenEntity(String type, String token, LocalDateTime expiresAt) {
        this.type = type;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public void addRefreshToken(RefreshTokenEntity refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void removeRefreshToken(RefreshTokenEntity refreshToken) {
        refreshToken.revoke();
        this.refreshToken = refreshToken;
    }

    public void revoke() {
        this.revoked = true;
    }
}
