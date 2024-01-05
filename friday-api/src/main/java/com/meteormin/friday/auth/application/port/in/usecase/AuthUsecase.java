package com.meteormin.friday.auth.application.port.in.usecase;

import com.meteormin.friday.auth.domain.Auth;
import com.meteormin.friday.auth.domain.Token;

public interface AuthUsecase {
    Token refreshToken(String token);

    void revokeToken();

    Auth signup(Auth auth);
}
