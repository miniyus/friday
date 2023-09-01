package com.miniyus.friday.infrastructure.oauth2;

import org.springframework.security.oauth2.core.user.OAuth2User;

import com.miniyus.friday.infrastructure.oauth2.userinfo.OAuth2UserInfo;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
public interface OAuth2lUserRepository {
    OAuth2UserInfo findByUserInfo(OAuth2UserInfo userInfo);

    OAuth2UserInfo save(OAuth2UserInfo user);
}
