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
import org.springframework.lang.NonNull;

/**
 * Access Token Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@RedisHash("access_token")
public class AccessTokenEntity {
    @Id
    @GeneratedValue
    private String id;

    @NonNull
    private String type;

    @Indexed
    @NonNull
    private String token;

    @Indexed
    @NonNull
    private String userId;

    @TimeToLive
    @NonNull
    private Long expiration;

    /**
     * @param type       token type, ex: Bearer
     * @param token      token
     * @param expiration token expiration seconds
     * @param userId     user id
     */
    public static AccessTokenEntity create(
        @NonNull String type,
        @NonNull String token,
        @NonNull Long userId,
        @NonNull Long expiration
    ) {
        return AccessTokenEntity.builder()
            .type(type)
            .token(token)
            .userId(userId.toString())
            .expiration(expiration)
            .build();
    }
}
