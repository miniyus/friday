package com.miniyus.friday.auth.application.port.in.query;

import com.miniyus.friday.auth.domain.Auth;

public interface RetrieveUserInfoQuery {
    Auth retrieveUserInfo();
}
