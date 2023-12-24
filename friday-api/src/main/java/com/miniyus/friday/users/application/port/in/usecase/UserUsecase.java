package com.miniyus.friday.users.application.port.in.usecase;

import com.miniyus.friday.users.domain.ResetPassword;
import com.miniyus.friday.users.domain.User;

public interface UserUsecase {
    void deleteUserById(Long id);

    User createUser(User request);

    User patchUser(User user);

    boolean resetPassword(ResetPassword resetPassword);
}
