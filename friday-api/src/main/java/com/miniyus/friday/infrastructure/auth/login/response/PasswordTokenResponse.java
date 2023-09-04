package com.miniyus.friday.infrastructure.auth.login.response;

import com.miniyus.friday.infrastructure.jwt.IssueToken;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@AllArgsConstructor
@Value
public class PasswordTokenResponse {
    private Long id;
    private String email;
    private String name;
    private IssueToken tokens;
}
