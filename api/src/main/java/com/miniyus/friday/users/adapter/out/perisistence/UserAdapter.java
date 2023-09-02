package com.miniyus.friday.users.adapter.out.perisistence;

import org.springframework.stereotype.Component;

import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.users.application.port.out.CreateUserPort;
import com.miniyus.friday.users.domain.User;

import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@RequiredArgsConstructor
@Component
public class UserAdapter implements CreateUserPort {
    private final UserRepository userRepository;

    @Override
    public User createUser(User domain) {
        var entity = userRepository.save(UserMapper.toEntity(domain));
        return UserMapper.toDomain(entity);
    }

}
