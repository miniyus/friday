package com.miniyus.friday.common;

import lombok.Getter;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Getter
public enum UserRole {
    ADMIN("ADMIN"), MANAGER("MANAGER"), USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

}
