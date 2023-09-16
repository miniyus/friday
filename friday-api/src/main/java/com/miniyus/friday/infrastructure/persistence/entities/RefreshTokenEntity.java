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
 * Refresh Token Entity
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
@AllArgsConstructor
@NoArgsConstructor
@RedisHash("refresh_token")
public class RefreshTokenEntity {
    @Id
    @GeneratedValue
    private String id;

    private String type;

    @Indexed
    private String token;

    @Indexed
    private String accessTokenId;

    @TimeToLive
    private Long expiration;


    /**
     * @param type          type
     * @param token         token
     * @param expiration    expiration seconds
     * @param accessTokenId access token id
     */
    @Builder
    public RefreshTokenEntity(String type, String token, String accessTokenId, Long expiration) {
        this.type = type;
        this.token = token;
        this.expiration = expiration;
        this.accessTokenId = accessTokenId;
    }
}
