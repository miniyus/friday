package com.miniyus.friday.domain.auth;

import lombok.Builder;
import lombok.Getter;

@Builder
public record Token(
    String tokenType,
    String accessToken,
    Long expiresIn,
    String refreshToken
) {
}
