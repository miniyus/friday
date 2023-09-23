package com.miniyus.friday.adapter.in.rest.resource;

import com.miniyus.friday.domain.users.User;
import java.io.Serializable;
import java.time.LocalDateTime;

public record UserResource(
    Long id,
    String email,
    String name,
    String role,
    String snsId,
    String provider,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) implements Serializable {

    public static UserResource fromDomain(User user) {
        return new UserResource(
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
