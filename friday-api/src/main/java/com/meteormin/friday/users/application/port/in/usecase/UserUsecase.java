package com.meteormin.friday.users.application.port.in.usecase;

import com.meteormin.friday.users.domain.CreateUser;
import com.meteormin.friday.users.domain.PatchUser;
import com.meteormin.friday.users.domain.ResetPassword;
import com.meteormin.friday.users.domain.User;

public interface UserUsecase {
    void deleteUserById(Long id);

    User createUser(CreateUser request);

    User patchUser(PatchUser user);

    boolean resetPassword(ResetPassword resetPassword);
}
