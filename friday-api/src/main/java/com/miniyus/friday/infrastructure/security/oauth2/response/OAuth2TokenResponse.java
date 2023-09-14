package com.miniyus.friday.infrastructure.security.oauth2.response;

import com.miniyus.friday.infrastructure.jwt.IssueToken;
import lombok.AllArgsConstructor;
import lombok.Value;

import java.io.Serializable;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
public record OAuth2TokenResponse(
    Long id,
    String snsId,
    String provider,
    IssueToken tokens) implements Serializable {
}
