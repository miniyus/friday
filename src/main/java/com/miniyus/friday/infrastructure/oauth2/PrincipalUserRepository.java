package com.miniyus.friday.infrastructure.oauth2;

import com.miniyus.friday.infrastructure.oauth2.userinfo.OAuth2UserInfo;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
public interface PrincipalUserRepository {
    PrincipalUserInfo findByUserInfo(OAuth2UserInfo userInfo);

    PrincipalUserInfo save(PrincipalUserInfo user);
}
