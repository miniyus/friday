package com.meteormin.friday.users.domain;

import com.meteormin.friday.common.util.JsonNullableUtil;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * User Business Domain
 *
 * @author meteormin
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
    private UserRole role;
    private String snsId;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime deletedAt;

    public static User create(
        CreateUser createUser
    ) {
        return User.builder()
            .email(createUser.email())
            .password(createUser.password())
            .name(createUser.name())
            .role(createUser.role())
            .build();
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
    public User update(String name, UserRole role) {
        this.name = name;
        this.role = role;

        return this;
    }

    /**
     * Updates the user object with the provided patch data.
     *
     * @param patch the patch object containing the updated user data
     */
    public User patch(PatchUser patch) {
        if (JsonNullableUtil.isPresent(patch.name())) {
            this.name = JsonNullableUtil.unwrap(patch.name(), null);
        }

        if (JsonNullableUtil.isPresent(patch.role())) {
            this.role = JsonNullableUtil.unwrap(patch.role(), null);
        }

        return this;
    }

    /**
     * Resets the password for the user.
     *
     * @param passwordString the new password to be set
     */
    public User resetPassword(String passwordString) {
        this.password = passwordString;

        return this;
    }

    /**
     * Deletes the current object by setting the "deletedAt" field to the current date and time.
     */
    public User delete() {
        this.deletedAt = LocalDateTime.now();
        return this;
    }
}
