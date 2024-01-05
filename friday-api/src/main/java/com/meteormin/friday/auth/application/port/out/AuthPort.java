package com.meteormin.friday.auth.application.port.out;

import com.meteormin.friday.auth.domain.Auth;
import com.meteormin.friday.auth.domain.Token;

public interface AuthPort {
    Token refreshToken(String token);

    Auth retrieveUserInfo();

    void revokeToken();

    Auth signup(Auth auth);
}
