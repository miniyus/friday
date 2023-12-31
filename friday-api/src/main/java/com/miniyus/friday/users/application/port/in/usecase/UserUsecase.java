package com.miniyus.friday.users.application.port.in.usecase;

import com.miniyus.friday.users.domain.CreateUser;
import com.miniyus.friday.users.domain.ResetPassword;
import com.miniyus.friday.users.domain.PatchUser;
import com.miniyus.friday.users.domain.User;

public interface UserUsecase {
    void deleteUserById(Long id);

    User createUser(CreateUser request);

    User patchUser(PatchUser user);

    boolean resetPassword(ResetPassword resetPassword);
}
