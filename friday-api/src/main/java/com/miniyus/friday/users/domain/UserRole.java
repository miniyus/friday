package com.miniyus.friday.users.domain;

import com.miniyus.friday.common.util.EnumUtil;
import com.miniyus.friday.common.util.HasText;
import lombok.Getter;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/09/02
 */
@Getter
public enum UserRole implements HasText {
    ADMIN, MANAGER, USER;

    @Override
    public String text() {
        return value();
    }

    @Override
    public String value() {
        return name().toLowerCase();
    }

    public static UserRole of(String value) {
        return EnumUtil.of(UserRole.class, value);
    }

    public static UserRole of(String value, boolean ignoreCase) {
        return EnumUtil.of(UserRole.class, value, ignoreCase);
    }
}
