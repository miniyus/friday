package com.meteormin.friday.users.domain;

import lombok.Builder;

@Builder
public record CreateUser(
    String email,
    String password,
    String name,
    UserRole role
) {
}
