package com.miniyus.friday.application.port.in.usecase;

import com.miniyus.friday.domain.users.ResetPassword;
import com.miniyus.friday.domain.users.UpdateUser;
import com.miniyus.friday.domain.users.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface UpdateUserUsecase {
    User patchUser(UpdateUser user);

    boolean resetPassword(ResetPassword resetPassword);
}
