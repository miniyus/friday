package com.miniyus.friday.infrastructure.jwt;

import lombok.AllArgsConstructor;
import lombok.Value;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
public record IssueToken(
    String accessToken,
    Long expiresIn,
    String refreshToken) {
}
