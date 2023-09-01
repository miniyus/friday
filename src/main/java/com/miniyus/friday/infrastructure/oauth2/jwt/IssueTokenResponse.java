package com.miniyus.friday.infrastructure.oauth2.jwt;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Data
@AllArgsConstructor
public class IssueTokenResponse {
    @JsonProperty("sns_id")
    private String snsId;

    @JsonProperty("provider")
    private String provider;

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("expires_in")
    private Long expiresIn;

    @JsonProperty("refresh_token")
    private String refreshToken;
}
