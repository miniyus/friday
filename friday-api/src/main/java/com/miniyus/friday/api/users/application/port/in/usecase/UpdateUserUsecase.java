package com.miniyus.friday.api.users.application.port.in.usecase;

import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserUsecase {
    UserResource patchUser(Long id,UpdateUserRequest request);

    UserResource resetPassword(Long id, String password);
}
