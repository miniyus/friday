package com.miniyus.friday.users.adapter.out.persistence.mapper;

import com.miniyus.friday.users.domain.UserRole;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import org.springframework.stereotype.Component;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Component
public class UserMapper {
    public UserEntity create(User domain) {
        return UserEntity.create(
            domain.getSnsId(),
            domain.getProvider(),
            domain.getEmail(),
            domain.getPassword(),
            domain.getName(),
            UserRole.valueOf(domain.getRole())
        );
    }

    public UserEntity toEntity(User domain) {
        return UserEntity.builder()
            .id(domain.getId())
            .snsId(domain.getSnsId())
            .provider(domain.getProvider())
            .email(domain.getEmail())
            .password(domain.getPassword())
            .name(domain.getName())
            .role(UserRole.valueOf(domain.getRole()))
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
            entity.getProvider(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt());
    }

}
