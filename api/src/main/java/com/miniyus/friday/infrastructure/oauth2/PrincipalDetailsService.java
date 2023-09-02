package com.miniyus.friday.infrastructure.oauth2;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.miniyus.friday.infrastructure.oauth2.userinfo.OAuth2UserInfo;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
public interface PrincipalDetailsService extends UserDetailsService {
    PrincipalUserInfo loadUser(OAuth2UserInfo userInfo);

    PrincipalUserInfo save(PrincipalUserInfo user);
}
