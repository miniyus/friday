package com.meteormin.friday.auth.adapter.out.persistence.mapper;

import com.meteormin.friday.auth.domain.Auth;
import com.meteormin.friday.auth.domain.Token;
import com.meteormin.friday.infrastructure.jwt.IssueToken;
import com.meteormin.friday.infrastructure.security.PrincipalUserInfo;
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
