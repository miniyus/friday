package com.miniyus.friday.api.users.application.port.out;

import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserPort {
    User updateUser(User user);

    User findById(Long id);

    User resetPassword(User user);
}
