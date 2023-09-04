package com.miniyus.friday.infrastructure.auth.oauth2.userinfo;

import java.util.Map;

import com.miniyus.friday.infrastructure.auth.oauth2.OAuth2Provider;

import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/31
 */
@Builder
@Getter
@RequiredArgsConstructor
public class NaverUserInfo implements OAuth2UserInfo {

    private final String snsId;

    private final String email;

    private final String name;

    private final Map<String, Object> attributes;

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }
}
