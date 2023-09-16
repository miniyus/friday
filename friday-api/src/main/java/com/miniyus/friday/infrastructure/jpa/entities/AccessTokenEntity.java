package com.miniyus.friday.infrastructure.jpa.entities;

import java.time.LocalDateTime;
import com.miniyus.friday.infrastructure.jpa.BaseEntity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;

/**
 * Access Token Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("accessToken")
public class AccessTokenEntity extends BaseEntity {
    @Id
    private Long id;

    private String type;

    @Indexed
    private String token;

    private LocalDateTime expiresAt;

    @TimeToLive
    private Long expiration;

    @Indexed
    private Long userId;

    /**
     *
     * @param type token type, ex: Bearer
     * @param token token
     * @param expiration  token expiration seconds
     * @param userId user id
     */
    @Builder
    public AccessTokenEntity(String type, String token, LocalDateTime expiresAt,Long expiration, Long userId) {
        this.type = type;
        this.token = token;
        this.expiresAt = expiresAt;
        this.expiration = expiration;
        this.expiresAt = LocalDateTime.now();
    }
}
