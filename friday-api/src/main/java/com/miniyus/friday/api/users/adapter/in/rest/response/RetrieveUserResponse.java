package com.miniyus.friday.api.users.adapter.in.rest.response;

import java.io.Serializable;
import java.time.LocalDateTime;

import com.miniyus.friday.api.users.domain.User;
import lombok.Builder;

@Builder
public record RetrieveUserResponse(
        Long id,
        String email,
        String name,
        String role,
        String snsId,
        String provider,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) implements Serializable {

    public static RetrieveUserResponse fromDomain(User user) {
        return new RetrieveUserResponse(
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
