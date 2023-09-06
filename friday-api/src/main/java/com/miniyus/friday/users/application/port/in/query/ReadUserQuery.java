package com.miniyus.friday.users.application.port.in.query;

import java.util.Collection;

import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface ReadUserQuery {
    Collection<User> findAll();

    Collection<User> findAll(ReadUserCommand command);

    User findById(Long id);
}
