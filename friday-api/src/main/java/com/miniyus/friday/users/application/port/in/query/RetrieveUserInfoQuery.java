package com.miniyus.friday.users.application.port.in.query;

import com.miniyus.friday.users.domain.Auth;

public interface RetrieveUserInfoQuery {
    Auth retrieveUserInfo();
}
