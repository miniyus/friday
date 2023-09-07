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
    private Long id;
    private String email;
    private String name;
    private String role;
    private String snsId;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
