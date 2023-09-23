package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.adapter.in.rest.resource.UserResource;
import com.miniyus.friday.adapter.in.rest.request.CreateUserRequest;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
public interface CreateUserUsecase {
    UserResource createUser(CreateUserRequest request);
}
