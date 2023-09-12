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
    String email;
    String password;
    String name;
    String role;
}
