package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.users.ResetPassword;
import com.miniyus.friday.domain.users.User;

public interface UserUsecase {
    void deleteUserById(Long id);

    User createUser(User request);

    User patchUser(User user);

    boolean resetPassword(ResetPassword resetPassword);
}
