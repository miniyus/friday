package com.miniyus.friday.users.adapter.out.persistence.mapper;

import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.social.SocialProvider;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserRole;
import org.springframework.stereotype.Component;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/09/02
 */
@Component
public class UserMapper {
    public UserEntity create(User domain) {
        return UserEntity.create(
            domain.getSnsId(),
            SocialProvider
                .ofElseNone(domain.getProvider(), true),
            domain.getEmail(),
            domain.getPassword(),
            domain.getName(),
            UserRole.of(domain.getRole(), true)
        );
    }

    public UserEntity toEntity(User domain) {
        return UserEntity.builder()
            .id(domain.getId())
            .snsId(domain.getSnsId())
            .provider(SocialProvider
                .ofElseNone(domain.getProvider(), true))
            .email(domain.getEmail())
            .password(domain.getPassword())
            .name(domain.getName())
            .role(UserRole.of(domain.getRole(), true))
            .deletedAt(domain.getDeletedAt())
            .build();
    }

    public User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getName(),
            entity.getRole().value(),
            entity.getSnsId(),
            entity.getProvider().value(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt());
    }

}
