package com.meteormin.friday.auth.application.port.in.query;

import com.meteormin.friday.auth.domain.Auth;

public interface RetrieveUserInfoQuery {
    Auth retrieveUserInfo();
}
