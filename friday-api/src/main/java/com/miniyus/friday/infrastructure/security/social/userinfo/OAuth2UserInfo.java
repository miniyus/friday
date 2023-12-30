package com.miniyus.friday.infrastructure.security.social.userinfo;

import com.miniyus.friday.infrastructure.security.social.SocialProvider;

import java.io.Serializable;
import java.util.Map;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/08/31
 */
public interface OAuth2UserInfo extends Serializable{
    String snsId();

    SocialProvider getProvider();

    String email();

    String name();

    Map<String, Object> attributes();
}
