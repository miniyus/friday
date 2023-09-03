package com.miniyus.friday.infrastructure.oauth2;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public enum UserRole {
    ADMIN("ADMIN"),
    MANAGER("MANAGER"),
    USER("USER");

    private final String value;

    UserRole(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
