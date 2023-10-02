package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.auth.Auth;

public interface SignupPort {
    Auth signup(Auth auth);
}
