package com.meteormin.friday.infrastructure.security.social.userinfo;

import com.meteormin.friday.infrastructure.security.social.SocialProvider;
import com.meteormin.friday.infrastructure.security.social.exception.NotSupportProviderException;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;

/**
 * OAuth2 attributes.
 *
 * @author meteormin
 * @since 2023/08/31
 */
@Getter
@Builder
@Slf4j
public class OAuth2Attributes {
    /**
     * email key
     */
    private static final String EMAIL_KEY = "email";

    /**
     * attributes
     */
    private Map<String, Object> attributes;

    /**
     * oauth login user id
     */
    private String id;

    /**
     * name attribute key
     */
    private String nameAttributeKey;

    /**
     * name
     */
    private String name;

    /**
     * email
     */
    private String email;

    /**
     * oauth2 provider
     *
     * @see SocialProvider
     */
    private String provider;

    public static OAuth2Attributes of(String registrationId, String userNameAttributeName,
        Map<String, Object> attributes) {

        log.debug("oauth provider : {}", registrationId);

        if ("naver".equals(registrationId)) {
            var naver = ofNaver(attributes);
            naver.provider = registrationId;
            return naver;
        } else if ("kakao".equals(registrationId)) {
            var kakao = ofKakao(attributes);
            kakao.provider = registrationId;
            return kakao;
        }

        var google = ofGoogle(userNameAttributeName, attributes);
        google.provider = registrationId;
        return google;
    }

    private static OAuth2Attributes ofGoogle(String userNameAttributeName,
        Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
            .id((String) attributes.get("sub"))
            .name((String) attributes.get("name"))
            .email((String) attributes.get(EMAIL_KEY))
            .attributes(attributes)
            .nameAttributeKey(userNameAttributeName)
            .build();
    }

    @SuppressWarnings("unchecked")
    private static OAuth2Attributes ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2Attributes.builder()
            .id((String) response.get("id"))
            .name((String) response.get("name"))
            .email((String) response.get(EMAIL_KEY))
            .attributes(response)
            .nameAttributeKey("id")
            .build();

    }

    @SuppressWarnings("unchecked")
    private static OAuth2Attributes ofKakao(Map<String, Object> attributes) {
        Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
        Map<String, Object> kakaoProfile = (Map<String, Object>) kakaoAccount.get("profile");
        return OAuth2Attributes.builder()
            .id((String) kakaoAccount.get("id"))
            .name((String) kakaoProfile.get("nickname"))
            .email((String) kakaoAccount.get(EMAIL_KEY))
            .attributes(kakaoAccount)
            .nameAttributeKey("id")
            .build();
    }

    /**
     * To user info o auth 2 user info.
     *
     * @return the o auth 2 user info
     */
    public OAuth2UserInfo toUserInfo() {
        try {
            SocialProvider socialProvider = SocialProvider.of(this.provider, true);
            return switch (socialProvider) {
                case NONE -> throw new NotSupportProviderException("error.notSupportProvider",
                    this.provider);
                case GOOGLE -> GoogleUserInfo.builder()
                    .email(email)
                    .snsId(id)
                    .name(name)
                    .attributes(attributes)
                    .build();
                case KAKAO -> KakaoUserInfo.builder()
                    .email(email)
                    .snsId(id)
                    .name(name)
                    .attributes(attributes)
                    .build();
                case NAVER -> NaverUserInfo.builder()
                    .email(email)
                    .snsId(id)
                    .name(name)
                    .attributes(attributes)
                    .build();
            };
        } catch (IllegalArgumentException e) {
            throw new NotSupportProviderException("error.notSupportProvider", this.provider);
        }
    }
}
