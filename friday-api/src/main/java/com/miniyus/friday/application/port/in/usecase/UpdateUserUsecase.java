package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.adapter.in.rest.resource.UserResource;
import com.miniyus.friday.adapter.in.rest.request.UpdateUserRequest;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserUsecase {
    UserResource patchUser(Long id, UpdateUserRequest request);

    UserResource resetPassword(Long id, String password);
}
