package com.miniyus.friday.infrastructure.persistence.entities;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
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
@RedisHash("access_token")
public class AccessTokenEntity {
    @Id
    @GeneratedValue
    private String id;

    private String type;

    @Indexed
    private String token;

    @Indexed
    private String userId;

    @TimeToLive
    private Long expiration;

    /**
     * @param type       token type, ex: Bearer
     * @param token      token
     * @param expiration token expiration seconds
     * @param userId     user id
     */
    @Builder
    public AccessTokenEntity(String type, String token, Long userId, Long expiration) {
        this.type = type;
        this.token = token;
        this.userId = userId.toString();
        this.expiration = expiration;
    }
}
