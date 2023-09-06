package com.miniyus.friday.infrastructure.auth.oauth2.response;

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
public class OAuth2TokenResponse {
    private Long id;

    private String snsId;

    private String provider;

    private IssueToken tokens;
}
