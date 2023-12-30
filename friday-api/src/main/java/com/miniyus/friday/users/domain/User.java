package com.miniyus.friday.users.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User Business Domain
 *
 * @author miniyus
 * @since 2023/09/02
 */
@Getter
@Builder
@AllArgsConstructor
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
    
    public static User create(
        String email,
        String password,
        String name,
        String role,
        String snsId,
        String provider
    ) {
        return new User(
            null,
            email,
            password,
            name,
            role,
            snsId,
            provider,
            null,
            null,
            null);
    }

    /**
     * Gets the role of the user.
     *
     * @return the role of the user as a string.
     */
    public String getRole() {
        return role.toUpperCase();
    }

    /**
     * Determines if the object has been deleted.
     *
     * @return true if the object has been deleted, false otherwise
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Determines if the user is an SNS user.
     *
     * @return true if the user is an SNS user, false otherwise
     */
    public boolean isSnsUser() {
        return snsId != null && provider != null;
    }

    /**
     * when call put method
     *
     * @param name user's name
     * @param role user's role
     */
    public void update(String name, String role) {
        this.name = name;
        this.role = role;
    }

    /**
     * when call patch method
     *
     * @param name user's name
     * @param role user's role
     */
    public void patch(String name, String role) {
        if (name != null)
            this.name = name;
        if (role != null)
            this.role = role;
    }

    /**
     * Resets the password for the user.
     *
     * @param passwordString the new password to be set
     */
    public void resetPassword(String passwordString) {
        this.password = passwordString;
    }

    /**
     * Deletes the current object by setting the "deletedAt" field to the current date and time.
     */
    public void delete() {
        this.deletedAt = LocalDateTime.now();
    }
}
