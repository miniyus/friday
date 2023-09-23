package com.miniyus.friday.adapter.in.rest.resource;

import com.miniyus.friday.domain.users.User;
import org.springframework.data.domain.Page;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

public record UserResources(
    List<UserResource> users
) implements Serializable{
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

    public static Page<UserResource> fromDomains(Page<User> users) {
        return users.map(UserResource::fromDomain);
    }

}
