package com.miniyus.friday.users.application.port.in.usecase;

import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserUsecase {
    public User patchUser(UpdateUserCommand command);

    public User resetPassword(Long id, String password);
}
