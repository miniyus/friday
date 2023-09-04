package com.miniyus.friday.infrastructure.auth.oauth2.userinfo;

import java.util.Map;

import com.miniyus.friday.infrastructure.auth.oauth2.OAuth2Provider;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/31
 */
@Builder
@Getter
@AllArgsConstructor
public class GoogleUserInfo implements OAuth2UserInfo {

    private final String snsId;

    private final String email;

    private final String name;

    private final Map<String, Object> attributes;

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
}
