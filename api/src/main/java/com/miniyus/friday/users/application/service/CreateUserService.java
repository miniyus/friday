package com.miniyus.friday.users.application.service;

import org.springframework.stereotype.Service;

import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
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
@Service
public class CreateUserService implements CreateUserUsecase {
    private final CreateUserPort createUserPort;

    public User createUser(CreateUserCommand command) {
        User user = new User(
                null,
                command.getEmail(),
                command.getPassword(),
                command.getName(),
                command.getRole(),
                null,
                null,
                null,
                null,
                null);

        return createUserPort.createUser(user);
    }
}
