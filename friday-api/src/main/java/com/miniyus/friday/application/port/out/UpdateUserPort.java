package com.miniyus.friday.application.port.out;

import com.miniyus.friday.domain.users.User;

import java.util.Optional;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserPort {
    User updateUser(User user);

    Optional<User> findById(Long id);

    User resetPassword(User user);
}
