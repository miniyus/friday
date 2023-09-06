package com.miniyus.friday.users.domain;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@AllArgsConstructor
@Getter
public class User {
    private Long id;
    private String email;
    private String password;
    private String name;
    private String role;
    private String snsId;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public boolean isDeleted() {
        return deletedAt != null;
    }

    public boolean isSnsUser() {
        return snsId != null && provider != null;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateRole(String role) {
        this.role = role;
    }

    public void resetPassword(String passwordString) {
        this.password = passwordString;
    }

    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }

    @RequiredArgsConstructor
    @Getter
    @Builder
    public static class SearchUser {
        private final String email;
        private final String name;
        private final LocalDateTime createdAtStart;
        private final LocalDateTime createdAtEnd;
        private final LocalDateTime updatedAtStart;
        private final LocalDateTime updatedAtEnd;

        public boolean isEmpty() {
            return email == null && name == null && createdAtStart == null && createdAtEnd == null
                    && updatedAtStart == null && updatedAtEnd == null;
        }
    }
}
