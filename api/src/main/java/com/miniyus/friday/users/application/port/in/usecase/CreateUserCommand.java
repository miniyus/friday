package com.miniyus.friday.users.application.port.in.usecase;

import lombok.Builder;
import lombok.Value;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@Builder
@Value
public class CreateUserCommand {
    private String email;
    private String password;
    private String name;
    private String role;
}
