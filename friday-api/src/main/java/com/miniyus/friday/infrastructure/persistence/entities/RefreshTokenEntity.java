package com.miniyus.friday.infrastructure.persistence.entities;

import jakarta.persistence.GeneratedValue;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;
import org.springframework.data.redis.core.index.Indexed;
import org.springframework.lang.NonNull;

/**
 * Refresh Token Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@Builder
@AllArgsConstructor
@RedisHash("refresh_token")
public class RefreshTokenEntity {
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
    private String accessTokenId;

    @TimeToLive
    @NonNull
    private Long expiration;


    /**
     * @param type          type
     * @param token         token
     * @param expiration    expiration seconds
     * @param accessTokenId access token id
     */
    public static RefreshTokenEntity create(
        @NonNull String type,
        @NonNull String token,
        @NonNull String accessTokenId,
        @NonNull Long expiration
    ) {
        return RefreshTokenEntity.builder()
            .type(type)
            .token(token)
            .expiration(expiration)
            .accessTokenId(accessTokenId)
            .build();
    }
}
