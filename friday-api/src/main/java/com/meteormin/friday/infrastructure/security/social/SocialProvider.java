package com.meteormin.friday.infrastructure.security.social;

import com.fasterxml.jackson.annotation.JsonValue;
import com.meteormin.friday.common.util.EnumUtil;
import com.meteormin.friday.common.util.HasText;
import lombok.Getter;

/**
 * social provider.
 *
 * @author seongminyoo
 * @since 2023/08/31
 */
@Getter
public enum SocialProvider implements HasText {
    NONE,
    GOOGLE,
    NAVER,
    KAKAO;

    @Override
    public String text() {
        return value();
    }

    @Override
    @JsonValue
    public String value() {
        return name().toLowerCase();
    }

    public static SocialProvider of(String value) {
        return EnumUtil.of(SocialProvider.class, value);
    }

    public static SocialProvider of(String value, boolean ignoreCase) {
        return EnumUtil.of(SocialProvider.class, value, ignoreCase);
    }

    public static SocialProvider ofElseNone(String value) {
        try {
            return EnumUtil.of(SocialProvider.class, value);
        } catch (IllegalArgumentException e) {
            return SocialProvider.NONE;
        }
    }

    public static SocialProvider ofElseNone(String value, boolean ignoreCase) {
        if (value == null) {
            return SocialProvider.NONE;
        }

        try {
            return EnumUtil.of(SocialProvider.class, value, ignoreCase);
        } catch (IllegalArgumentException e) {
            return SocialProvider.NONE;
        }
    }
}
