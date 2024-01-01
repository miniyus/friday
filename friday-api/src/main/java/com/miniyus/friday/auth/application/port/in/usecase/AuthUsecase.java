package com.miniyus.friday.auth.application.port.in.usecase;

import com.miniyus.friday.auth.domain.Auth;
import com.miniyus.friday.auth.domain.Token;

public interface AuthUsecase {
    Token refreshToken(String token);

    void revokeToken();

    Auth signup(Auth auth);
}
