package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.auth.Auth;
import com.miniyus.friday.domain.auth.Token;

public interface AuthUsecase {
    Token refreshToken(String token);

    void revokeToken();

    Auth signup(Auth auth);
}
