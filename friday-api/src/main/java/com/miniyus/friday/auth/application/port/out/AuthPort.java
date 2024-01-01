package com.miniyus.friday.auth.application.port.out;

import com.miniyus.friday.auth.domain.Auth;
import com.miniyus.friday.auth.domain.Token;

public interface AuthPort {
    Token refreshToken(String token);

    Auth retrieveUserInfo();

    void revokeToken();

    Auth signup(Auth auth);
}
