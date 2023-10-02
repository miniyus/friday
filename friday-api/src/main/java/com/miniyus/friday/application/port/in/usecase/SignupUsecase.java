package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.auth.Auth;

public interface SignupUsecase {
    Auth signup(Auth auth);
}
