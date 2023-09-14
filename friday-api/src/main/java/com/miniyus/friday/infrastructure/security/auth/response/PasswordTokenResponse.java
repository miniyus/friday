package com.miniyus.friday.infrastructure.security.auth.response;

import com.miniyus.friday.infrastructure.jwt.IssueToken;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
public record PasswordTokenResponse(
        Long id,
        String email,
        String name,
        IssueToken tokens) {
}
