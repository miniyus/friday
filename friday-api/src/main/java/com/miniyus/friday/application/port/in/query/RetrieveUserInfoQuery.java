package com.miniyus.friday.application.port.in.query;

import com.miniyus.friday.domain.auth.Auth;

public interface RetrieveUserInfoQuery {
    Auth retrieveUserInfo();
}
