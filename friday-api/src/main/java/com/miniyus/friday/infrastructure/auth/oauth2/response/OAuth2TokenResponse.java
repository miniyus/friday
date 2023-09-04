package com.miniyus.friday.infrastructure.auth.oauth2.response;

import com.fasterxml.jackson.annotation.JsonProperty;
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
    @JsonProperty("id")
    private Long id;

    @JsonProperty("sns_id")
    private String snsId;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("tokens")
    private IssueToken tokens;
}
