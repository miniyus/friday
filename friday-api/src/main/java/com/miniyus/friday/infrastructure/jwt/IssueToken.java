package com.miniyus.friday.infrastructure.jwt;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.io.Serializable;

/**
 * [description]
 *
 * @author seongminyoo
 * @since 2023/08/31
 */
@JsonIgnoreProperties({
    "accessTokenKey",
    "refreshTokenKey"
})
public record IssueToken(
        String tokenType,
        String accessTokenKey,
        String accessToken,
        Long expiresIn,
        String refreshTokenKey,
        String refreshToken) implements Serializable {
}
