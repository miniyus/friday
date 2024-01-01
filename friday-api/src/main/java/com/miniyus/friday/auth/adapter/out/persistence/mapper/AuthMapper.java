package com.miniyus.friday.auth.adapter.out.persistence.mapper;

import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.auth.domain.Auth;
import com.miniyus.friday.auth.domain.Token;
import org.springframework.stereotype.Component;

@Component
public class AuthMapper {
    public Auth toAuthDomain(PrincipalUserInfo userInfo) {
        return Auth.builder()
            .id(userInfo.getId())
            .name(userInfo.getName())
            .email(userInfo.getEmail())
            .role(userInfo.getRole())
            .snsId(userInfo.getSnsId())
            .provider(userInfo.getProvider().value())
            .build();
    }

    public Token toTokenDomain(IssueToken issueToken) {
        return Token.builder()
            .tokenType(issueToken.tokenType())
            .accessToken(issueToken.accessToken())
            .expiresIn(issueToken.expiresIn())
            .refreshToken(issueToken.refreshToken())
            .build();
    }
}
