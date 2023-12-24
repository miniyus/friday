package com.miniyus.friday.infrastructure.security.social.userinfo;

import java.io.Serializable;
import java.util.Map;
import com.miniyus.friday.infrastructure.security.social.OAuth2Provider;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/31
 */
public interface OAuth2UserInfo extends Serializable{
    String snsId();

    OAuth2Provider getProvider();

    String email();

    String name();

    Map<String, Object> attributes();
}
