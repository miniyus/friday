package com.meteormin.friday.infrastructure.security.social.response;

import com.meteormin.friday.infrastructure.jwt.IssueToken;

import java.io.Serializable;

/**
 * [description]
 *
 * @author seongminyoo
 * @since 2023/09/04
 */
public record OAuth2TokenResponse(
        Long id,
        String snsId,
        String provider,
        String email,
        IssueToken tokens) implements Serializable {
}
