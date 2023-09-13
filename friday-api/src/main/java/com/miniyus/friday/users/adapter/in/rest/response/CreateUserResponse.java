package com.miniyus.friday.users.adapter.in.rest.response;

import java.time.LocalDateTime;

import com.miniyus.friday.users.domain.User;

import lombok.Builder;
import lombok.Value;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Value
@Builder
public class CreateUserResponse {
    Long id;
    String email;
    String name;
    String role;
    String snsId;
    String provider;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

    public static CreateUserResponse fromDomain(User user) {
        return new CreateUserResponse(
                user.getId(),
                user.getEmail(),
                user.getName(),
                user.getRole(),
                user.getSnsId(),
                user.getProvider(),
                user.getCreatedAt(),
                user.getUpdatedAt());
    }
}
