package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.users.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface CreateUserUsecase {
    User createUser(User request);
}
