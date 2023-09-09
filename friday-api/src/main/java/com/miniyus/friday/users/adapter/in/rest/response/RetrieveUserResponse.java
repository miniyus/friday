package com.miniyus.friday.users.adapter.in.rest.response;

import java.time.LocalDateTime;
import com.miniyus.friday.users.domain.User;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class RetrieveUserResponse {
    private Long id;
    private String email;
    private String name;
    private String role;
    private String snsId;
    private String provider;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

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
