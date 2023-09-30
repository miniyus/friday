package com.miniyus.friday.adapter.out.persistence.mapper;

import com.miniyus.friday.common.UserRole;
import org.springframework.stereotype.Component;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.domain.users.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Component
public class UserMapper {

    public UserEntity toEntity(User domain) {
        return new UserEntity(
            domain.getId(),
            domain.getSnsId(),
            domain.getProvider(),
            domain.getEmail(),
            domain.getPassword(),
            domain.getName(),
            UserRole.valueOf(domain.getRole()),
            domain.getDeletedAt());
    }

    public User toDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getName(),
            entity.getRole().getValue(),
            entity.getSnsId(),
            entity.getProvider(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt());
    }

}
