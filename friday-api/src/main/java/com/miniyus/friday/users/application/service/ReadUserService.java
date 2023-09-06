package com.miniyus.friday.users.application.service;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import com.miniyus.friday.users.application.port.in.query.ReadUserCommand;
import com.miniyus.friday.users.application.port.in.query.ReadUserQuery;
import com.miniyus.friday.users.application.port.out.ReadUserPort;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.User.SearchUser;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
@Service
@RequiredArgsConstructor
public class ReadUserService implements ReadUserQuery {
    private final ReadUserPort readUserPort;

    @Override
    public Collection<User> findAll() {
        return readUserPort.findAll();
    }

    @Override
    public Page<User> findAll(ReadUserCommand command) {
        // find by conditions
        SearchUser search = User.SearchUser.builder()
                .email(command.getEmail())
                .name(command.getName())
                .createdAtStart(command.getCreatedAtStart())
                .createdAtEnd(command.getCreatedAtEnd())
                .updatedAtStart(command.getUpdatedAtStart())
                .updatedAtEnd(command.getUpdatedAtEnd())
                .build();

        // only paginate
        if (search.isEmpty()) {
            return readUserPort.findAll(command.getPageable());
        }

        return readUserPort.findAll(search, command.getPageable());
    }

    @Override
    public User findById(Long id) {
        return readUserPort.findById(id);
    }
}
