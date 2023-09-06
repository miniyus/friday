package com.miniyus.friday.infrastructure.jwt;

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
    private String accessToken;

    private Long expiresIn;

    private String refreshToken;
}
