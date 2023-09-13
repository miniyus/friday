package com.miniyus.friday.users.adapter.in.rest.response;

import java.time.LocalDateTime;
import com.miniyus.friday.users.domain.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RetrieveUserResponse {
    Long id;
    String email;
    String name;
    String role;
    String snsId;
    String provider;
    LocalDateTime createdAt;
    LocalDateTime updatedAt;

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
