package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.auth.Auth;
import com.miniyus.friday.domain.auth.Token;

public interface AuthPort {
    Token refreshToken(String token);

    Auth retrieveUserInfo();

    void revokeToken();

    Auth signup(Auth auth);
}
