package com.miniyus.friday.users.application.port.in.query;

import java.util.Collection;

import org.springframework.data.domain.Page;

import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/06
 */
public interface ReadUserQuery {
    Collection<User> findAll();

    Page<User> findAll(ReadUserCommand command);

    User findById(Long id);
}
