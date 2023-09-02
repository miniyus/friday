package com.miniyus.friday.users.application.port.out;

import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface CreateUserPort {
    User createUser(User command);
}
