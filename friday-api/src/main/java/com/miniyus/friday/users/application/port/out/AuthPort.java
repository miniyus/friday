package com.miniyus.friday.users.application.port.out;

import com.miniyus.friday.users.domain.Auth;
import com.miniyus.friday.users.domain.Token;

public interface AuthPort {
    Token refreshToken(String token);

    Auth retrieveUserInfo();

    void revokeToken();

    Auth signup(Auth auth);
}
