package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.auth.Token;

public interface RefreshTokenPort {
    Token refreshToken(String token);
}
