package com.miniyus.friday.infrastructure.security.social;

import lombok.Getter;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Getter
public enum OAuth2Provider {
    GOOGLE("google"), NAVER("naver"), KAKAO("kakao");

    private final String value;

    OAuth2Provider(String value) {
        this.value = value;
    }

    public static OAuth2Provider of(String registrationId) {
        return switch (registrationId) {
            case "google" -> OAuth2Provider.GOOGLE;
            case "kakao" -> OAuth2Provider.KAKAO;
            case "naver" -> OAuth2Provider.NAVER;
            default -> throw new IllegalArgumentException(
                String.format("OAuthProvider: missmatch %s", registrationId));
        };
    }
}
