package com.miniyus.friday.api.users.adapter.out.persistence;

import org.springframework.stereotype.Component;

import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Component
public class UserMapper {

    public static UserEntity toEntity(User domain) {
        return new UserEntity(
                domain.getId(),
                domain.getSnsId(),
                domain.getProvider(),
                domain.getEmail(),
                domain.getPassword(),
                domain.getName(),
                domain.getRole(),
                domain.getDeletedAt());
    }

    public static User toDomain(UserEntity entity) {
        return new User(
                entity.getId(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getName(),
                entity.getRole(),
                entity.getSnsId(),
                entity.getProvider(),
                entity.getCreatedAt(),
                entity.getUpdatedAt(),
                entity.getDeletedAt());
    }

}