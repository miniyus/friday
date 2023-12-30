package com.miniyus.friday.infrastructure.security.social.userinfo;

import com.miniyus.friday.infrastructure.security.social.SocialProvider;
import lombok.Builder;

import java.util.Map;

/**
 * google user
 *
 * @author miniyus
 * @since 2023/08/31
 */
@Builder
public record GoogleUserInfo(
        String snsId,
        String email,
        String name,
        Map<String, Object> attributes) implements OAuth2UserInfo {

    @Override
    public SocialProvider getProvider() {
        return SocialProvider.GOOGLE;
    }
}
