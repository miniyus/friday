package com.miniyus.friday.infrastructure.security.social;

import com.miniyus.friday.common.util.EnumUtil;
import com.miniyus.friday.common.util.HasText;
import lombok.Getter;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Getter
public enum SocialProvider implements HasText {
    GOOGLE,
    NAVER,
    KAKAO;

    @Override
    public String text() {
        return value();
    }

    @Override
    public String value() {
        return name().toLowerCase();
    }

    public static SocialProvider of(String value) {
        return EnumUtil.of(SocialProvider.class, value);
    }

    public static SocialProvider of(String value, boolean ignoreCase) {
        return EnumUtil.of(SocialProvider.class, value, ignoreCase);
    }
}
