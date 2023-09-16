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
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Refresh Token Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("refreshToken")
public class RefreshTokenEntity extends BaseEntity {
    @Id
    private Long id;

    private String type;

    @Indexed
    private String token;

    private LocalDateTime expiresAt;

    @TimeToLive
    private Long expiration;

    @Indexed
    private Long accessTokenId;

    /**
     *
     * @param type type
     * @param token token
     * @param expiration expiration seconds
     * @param accessTokenId access token id
     */
    @Builder
    public RefreshTokenEntity(String type, String token,LocalDateTime expiresAt,Long expiration, Long accessTokenId) {
        this.type = type;
        this.token = token;
        this.expiresAt = expiresAt;
        this.expiration = expiration;
        this.accessTokenId = accessTokenId;
    }
}
