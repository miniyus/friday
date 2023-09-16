package com.miniyus.friday.api.users.application.port.in.usecase;

import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserUsecase {
    User patchUser(Long id,UpdateUserRequest request);

    User resetPassword(Long id, String password);
}
