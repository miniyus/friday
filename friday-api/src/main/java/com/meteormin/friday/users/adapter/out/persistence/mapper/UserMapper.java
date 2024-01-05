package com.meteormin.friday.users.adapter.out.persistence.mapper;

import com.meteormin.friday.infrastructure.persistence.entities.UserEntity;
import com.meteormin.friday.infrastructure.security.social.SocialProvider;
import com.meteormin.friday.users.domain.User;
import org.springframework.stereotype.Component;

/**
 * User mapper
 *
 * @author meteormin
 * @since 2023/09/02
 */
@Component
public class UserMapper {
    public UserEntity createUserEntity(User domain) {
        return UserEntity.create(
            domain.getSnsId(),
            SocialProvider
                .ofElseNone(domain.getProvider(), true),
            domain.getEmail(),
            domain.getPassword(),
            domain.getName(),
            domain.getRole()
        );
    }

    public UserEntity toUserEntity(User domain) {
        return UserEntity.builder()
            .id(domain.getId())
            .snsId(domain.getSnsId())
            .provider(SocialProvider
                .ofElseNone(domain.getProvider(), true))
            .email(domain.getEmail())
            .password(domain.getPassword())
            .name(domain.getName())
            .role(domain.getRole())
            .deletedAt(domain.getDeletedAt())
            .build();
    }

    public User toUserDomain(UserEntity entity) {
        return new User(
            entity.getId(),
            entity.getEmail(),
            entity.getPassword(),
            entity.getName(),
            entity.getRole(),
            entity.getSnsId(),
            entity.getProvider().value(),
            entity.getCreatedAt(),
            entity.getUpdatedAt(),
            entity.getDeletedAt());
    }

}
