package com.miniyus.friday.users.adapter.in.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/11
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ResetPasswordRequest {
    @NotBlank
    @Size(min = 8, max = 50)
    private String password;
}
