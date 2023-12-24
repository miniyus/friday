package com.miniyus.friday.users.application.port.in.usecase;

import com.miniyus.friday.users.domain.Auth;
import com.miniyus.friday.users.domain.Token;

public interface AuthUsecase {
    Token refreshToken(String token);

    void revokeToken();

    Auth signup(Auth auth);
}
