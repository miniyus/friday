package com.miniyus.friday.application.service;

import com.miniyus.friday.application.port.in.query.RetrieveUserInfoQuery;
import com.miniyus.friday.application.port.in.usecase.RefreshTokenUsecase;
import com.miniyus.friday.application.port.in.usecase.RevokeTokenUsecase;
import com.miniyus.friday.application.port.in.usecase.SignupUsecase;
import com.miniyus.friday.application.port.out.RefreshTokenPort;
import com.miniyus.friday.application.port.out.RetrieveUserInfoPort;
import com.miniyus.friday.application.port.out.RevokeTokenPort;
import com.miniyus.friday.application.port.out.SignupPort;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.domain.auth.Auth;
import com.miniyus.friday.domain.auth.Token;
import lombok.RequiredArgsConstructor;

@Usecase
@RequiredArgsConstructor
public class AuthService implements SignupUsecase, RefreshTokenUsecase, RevokeTokenUsecase,RetrieveUserInfoQuery {
    private final SignupPort signupPort;
    private final RetrieveUserInfoPort retrieveUserInfoPort;
    private final RefreshTokenPort refreshTokenPort;
    private final RevokeTokenPort revokeTokenPort;

    @Override
    public Auth retrieveUserInfo() {
        return retrieveUserInfoPort.retrieveUserInfo();
    }

    @Override
    public Token refreshToken(String token) {
        return refreshTokenPort.refreshToken(token);
    }

    @Override
    public Auth signup(Auth auth) {
        return signupPort.signup(auth);
    }

    @Override
    public void revokeToken() {
        revokeTokenPort.revokeToken();
    }
}
