package com.miniyus.friday.users.adapter.out.persistence;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.users.application.port.out.AuthPort;
import com.miniyus.friday.users.domain.Auth;
import com.miniyus.friday.users.domain.Token;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@PersistenceAdapter
@Transactional
public class AuthAdapter implements AuthPort {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Auth signup(Auth authentication) {
        try {
            userDetailsService.setPasswordEncoder(passwordEncoder);
            var principalUserInfo = userDetailsService.create(
               PasswordUserInfo.builder()
                   .email(authentication.getEmail())
                   .name(authentication.getName())
                   .password(passwordEncoder.encode(authentication.getPassword()))
                   .build()
                );
            return Auth.builder()
                .id(principalUserInfo.getId())
                .name(principalUserInfo.getName())
                .email(principalUserInfo.getEmail())
                .role(principalUserInfo.getRole())
                .build();
        } catch (Throwable throwable) {
            throw new RestErrorException("error.userExists", RestErrorCode.CONFLICT);
        }
    }

    @Override
    public Token refreshToken(String refreshToken) {
        var tokenInfo = refreshToken.split(" ");
        var tokenType = tokenInfo[0];
        var token = tokenInfo[1];

        if (!tokenType.equals(JwtProvider.BEARER)) {
            throw new RestErrorException(
                "error.unsupportedTokenType",
                RestErrorCode.UNSUPPORTED_TOKEN_TYPE
            );
        }

        UserEntity user = jwtService.getUserByRefreshToken(token).orElse(null);
        if (user == null) {
            throw new RestErrorException(
                "error.userNotFound",
                RestErrorCode.ACCESS_DENIED
            );
        }

        var issueToken = jwtService.issueToken(user.getId());

        return Token.builder()
            .tokenType(issueToken.tokenType())
            .accessToken(issueToken.accessToken())
            .expiresIn(issueToken.expiresIn())
            .refreshToken(issueToken.refreshToken())
            .build();
    }

    private PrincipalUserInfo userInfo() {
        return (PrincipalUserInfo) SecurityContextHolder
            .getContext()
            .getAuthentication()
            .getPrincipal();
    }

    @Override
    public Auth retrieveUserInfo() {
        var user = userInfo();
        if (user == null) {
            throw userNotExists();
        }

        return Auth.builder()
            .id(user.getId())
            .role(user.getRole())
            .email(user.getEmail())
            .name(user.getName())
            .snsId(user.getSnsId())
            .provider(user.getProvider().value())
            .build();
    }

    @Override
    public void revokeToken() {
        var user = userInfo();
        if (user == null) {
            throw userNotExists();
        }

        if (user.getId() == null) {
            throw userNotExists();
        }

        jwtService.revokeTokenByUserId(user.getId());
    }

    private RestErrorException userNotExists() {
        return new RestErrorException(
            "error.userNotExists",
            RestErrorCode.NOT_FOUND
        );
    }
}
