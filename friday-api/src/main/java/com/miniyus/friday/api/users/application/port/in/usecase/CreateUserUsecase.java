package com.miniyus.friday.api.users.application.port.in.usecase;

import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface CreateUserUsecase {
    UserResource createUser(CreateUserRequest request);
}
