package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.auth.Auth;

public interface RetrieveUserInfoPort {
    Auth retrieveUserInfo();
}
