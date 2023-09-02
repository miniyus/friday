package com.miniyus.friday.infrastructure.oauth2;

import com.miniyus.friday.infrastructure.oauth2.jwt.IssueTokenResponse;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
public interface JwtService {
    IssueTokenResponse issueToken(PrincipalUserInfo userInfo);
}
