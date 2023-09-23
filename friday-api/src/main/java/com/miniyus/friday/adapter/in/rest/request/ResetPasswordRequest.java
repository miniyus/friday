package com.miniyus.friday.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import java.io.Serializable;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/11
 */
@Builder
public record ResetPasswordRequest(
        @NotBlank(message = "validation.user.password.notBlank") @Size(min = 8, max = 50,
                message = "validation.user.password.size") String password)
        implements Serializable {
}
