package com.miniyus.friday.auth;

import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
@Transactional
public class AuthService {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public PrincipalUserInfo signup(PasswordUserInfo authentication) {
        try {
            userDetailsService.setPasswordEncoder(passwordEncoder);
            return userDetailsService.create(authentication);
        } catch (Throwable throwable) {
            throw new RestErrorException("error.userExists", RestErrorCode.CONFLICT);
        }
    }

    public IssueToken refresh(String refreshToken) {
        var tokenInfo = refreshToken.split(" ");
        var tokenType = tokenInfo[0];
        var token = tokenInfo[1];

        if (!tokenType.equals(JwtProvider.BEARER)) {
            throw new RestErrorException(
                "validation.auth.invalidTokenType",
                AuthErrorCode.INVALID_TOKEN
            );
        }

        UserEntity user = jwtService.getUserByRefreshToken(token).orElse(null);
        if (user == null) {
            throw new RestErrorException(
                "error.userNotFound",
                AuthErrorCode.ACCESS_DENIED
            );
        }

        return jwtService.issueToken(user.getId());
    }

    public PrincipalUserInfo userInfo() {
        return (PrincipalUserInfo) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
    }

    public void revokeToken() {
        var user = userInfo();
        if (user == null) {
            throw new RestErrorException(
                "error.userNotExists",
                RestErrorCode.NOT_FOUND
            );
        }

        if (user.getId() == null) {
            throw new RestErrorException(
                "error.userNotExists",
                RestErrorCode.NOT_FOUND
            );
        }

        jwtService.revokeTokenByUserId(user.getId());
    }
}
