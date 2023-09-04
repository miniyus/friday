package com.miniyus.friday.infrastructure.auth.oauth2;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
public enum OAuth2Provider {
    GOOGLE("google"),
    NAVER("naver"),
    KAKAO("kakao");

    private final String id;

    OAuth2Provider(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public static OAuth2Provider of(String registrationId) {
        switch (registrationId) {
            case "google":
                return OAuth2Provider.GOOGLE;
            case "kakao":
                return OAuth2Provider.KAKAO;
            case "naver":
                return OAuth2Provider.NAVER;
            default:
                throw new IllegalArgumentException(String.format("OAuthProvider: missmatch {}", registrationId));
        }
    }
}
