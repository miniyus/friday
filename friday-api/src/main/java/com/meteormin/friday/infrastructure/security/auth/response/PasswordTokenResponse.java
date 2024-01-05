package com.meteormin.friday.infrastructure.security.auth.response;

import com.meteormin.friday.infrastructure.jwt.IssueToken;

/**
 * [description]
 *
 * @author seongminyoo
 * @since 2023/09/04
 */
public record PasswordTokenResponse(
        Long id,
        String email,
        String name,
        IssueToken tokens
) {
}
