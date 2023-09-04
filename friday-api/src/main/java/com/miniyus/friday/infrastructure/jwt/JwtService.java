package com.miniyus.friday.infrastructure.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * JWTService
 * 
 * @author seongminyoo
 * @date 2023/08/31
 */
@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public IssueToken issueToken(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).get();

        if (userEntity == null) {
            throw new AccessDeniedException("Cannot find user");
        }

        AccessTokenEntity accessToken = createAccessToken(userEntity);
        return new IssueToken(
                accessToken.getToken(),
                jwtProvider.getAccessTokenExpiration(),
                accessToken.getRefreshToken().getToken());
    }

    public IssueToken issueToken(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).get();

        if (userEntity == null) {
            throw new AccessDeniedException("Cannot find user");
        }

        AccessTokenEntity accessToken = createAccessToken(userEntity);
        return new IssueToken(
                accessToken.getToken(),
                jwtProvider.getAccessTokenExpiration(),
                accessToken.getRefreshToken().getToken());
    }

    private AccessTokenEntity createAccessToken(UserEntity userEntity) {
        String token = jwtProvider.createAccessToken(userEntity.getEmail());

        Date expiresAt = jwtProvider.extractExpiresAt(token).get();
        LocalDateTime exp = expiresAt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        AccessTokenEntity tokenEntity = AccessTokenEntity.builder()
                .type("Bearer")
                .token(token)
                .expiresAt(exp)
                .build();

        RefreshTokenEntity refreshToken = createRefreshToken(tokenEntity);

        tokenEntity.addRefreshToken(refreshToken);
        userEntity.addAccessToken(tokenEntity);
        userRepository.save(userEntity);
        return tokenEntity;
    }

    private RefreshTokenEntity createRefreshToken(AccessTokenEntity tokenEntity) {
        String refreshToken = jwtProvider.createRefreshToken();
        Date expiresAt = jwtProvider.extractExpiresAt(refreshToken).get();
        LocalDateTime exp = expiresAt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshTokenEntity refreshTokenEntity = RefreshTokenEntity.builder()
                .type("Bearer")
                .token(refreshToken)
                .expiresAt(exp)
                .build();

        return refreshTokenEntity;
    }

    public UserEntity getUserByRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity = refreshTokenRepository.findByToken(refreshToken);
        if (refreshTokenEntity == null) {
            throw new AccessDeniedException("Invalid refresh token");
        }

        return refreshTokenEntity
                .getAccessToken()
                .getUser();
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return jwtProvider.extractAccessToken(request);
    }

    public Optional<String> extractEmail(String accessToken) {
        return jwtProvider.extractEmail(accessToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return jwtProvider.extractRefreshToken(request);
    }

    public boolean isTokenValid(String token) {
        return jwtProvider.isTokenValid(token);
    }
}
