package com.miniyus.friday.infrastructure.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Value
@AllArgsConstructor
public class IssueToken {
    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
