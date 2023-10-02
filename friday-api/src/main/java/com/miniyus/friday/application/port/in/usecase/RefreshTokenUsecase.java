package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.auth.Token;

public interface RefreshTokenUsecase {
    Token refreshToken(String token);
}
