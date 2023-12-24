package com.miniyus.friday.infrastructure.security.social.response;

import com.miniyus.friday.infrastructure.jwt.IssueToken;
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
        String email,
        IssueToken tokens) implements Serializable {
}
