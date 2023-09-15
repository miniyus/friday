package com.miniyus.friday.api.users.application.port.out;

import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface CreateUserPort extends UpdateUserPort {
    User createUser(User user);

    boolean isUniqueEmail(String email);
}
