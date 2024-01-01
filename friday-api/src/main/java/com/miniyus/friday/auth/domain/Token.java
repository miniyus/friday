package com.miniyus.friday.auth.domain;

import lombok.Builder;

@Builder
public record Token(
    String tokenType,
    String accessToken,
    Long expiresIn,
    String refreshToken
) {
}
