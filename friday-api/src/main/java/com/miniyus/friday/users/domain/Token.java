package com.miniyus.friday.users.domain;

import lombok.Builder;

@Builder
public record Token(
    String tokenType,
    String accessToken,
    Long expiresIn,
    String refreshToken
) {
}
