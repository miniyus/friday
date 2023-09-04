package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;
import org.hibernate.annotations.Where;
import com.miniyus.friday.infrastructure.jpa.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@AllArgsConstructor
@NoArgsConstructor
@Where(clause = "revoked = false")
@Table(name = "refresh_token")
public class RefreshTokenEntity extends BaseEntity {
    @Id
    @GeneratedValue
    private Long id;

    private String type;

    private String token;

    private LocalDateTime expiresAt;

    private boolean revoked;

    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "access_token_id")
    private AccessTokenEntity accessToken;

    /**
     * @param type
     * @param token
     * @param expiresAt
     */
    @Builder
    public RefreshTokenEntity(String type, String token, LocalDateTime expiresAt) {
        this.type = type;
        this.token = token;
        this.expiresAt = expiresAt;
    }

    public void revoke() {
        this.revoked = true;
    }
}
