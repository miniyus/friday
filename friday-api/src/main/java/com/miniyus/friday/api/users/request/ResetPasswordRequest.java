package com.miniyus.friday.api.users.request;

import com.miniyus.friday.users.domain.ResetPassword;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.io.Serializable;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/09/11
 */
@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "validation.user.password.notBlank") @Size(min = 8, max = 50,
                message = "validation.user.password.size") String password)
        implements Serializable {

    public ResetPassword toDomain(Long id) {
        return new ResetPassword(id,password);
    }
}
