package com.miniyus.friday.infrastructure.security.oauth2.userinfo;

import java.util.Map;

import com.miniyus.friday.infrastructure.security.oauth2.OAuth2Provider;
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
public record GoogleUserInfo(
    String snsId,
    String email,
    String name,
    Map<String, Object> attributes) implements OAuth2UserInfo {

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.GOOGLE;
    }
}
