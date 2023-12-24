package com.miniyus.friday.users.application.service;

import com.miniyus.friday.users.application.port.in.query.RetrieveUserInfoQuery;
import com.miniyus.friday.users.application.port.in.usecase.AuthUsecase;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.users.application.port.out.AuthPort;
import com.miniyus.friday.users.domain.Auth;
import com.miniyus.friday.users.domain.Token;
import lombok.RequiredArgsConstructor;

@Usecase
@RequiredArgsConstructor
public class AuthService implements RetrieveUserInfoQuery, AuthUsecase {
    private final AuthPort authPort;

    @Override
    public Auth retrieveUserInfo() {
        return authPort.retrieveUserInfo();
    }

    @Override
    public Token refreshToken(String token) {
        return authPort.refreshToken(token);
    }

    @Override
    public Auth signup(Auth auth) {
        return authPort.signup(auth);
    }

    @Override
    public void revokeToken() {
        authPort.revokeToken();
    }
}
