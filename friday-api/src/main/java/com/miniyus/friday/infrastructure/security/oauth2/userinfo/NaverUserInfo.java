package com.miniyus.friday.infrastructure.security.oauth2.userinfo;

import java.util.Map;
import com.miniyus.friday.infrastructure.security.oauth2.OAuth2Provider;
import lombok.Builder;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/31
 */
@Builder
public record NaverUserInfo(
        String snsId,
        String email,
        String name,
        Map<String, Object> attributes) implements OAuth2UserInfo {

    @Override
    public OAuth2Provider getProvider() {
        return OAuth2Provider.NAVER;
    }

}
