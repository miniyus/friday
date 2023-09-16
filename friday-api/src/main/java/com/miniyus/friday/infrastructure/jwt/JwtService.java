package com.miniyus.friday.infrastructure.jwt;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;
import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import jakarta.validation.constraints.NotNull;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.AccessTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

/**
 * JWT Service * issue token * refresh token
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Service
@RequiredArgsConstructor
@Transactional
public class JwtService {
    private final JwtProvider jwtProvider;
    private final UserRepository userRepository;
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    /**
     * Generates a token for the given user ID.
     *
     * @param userId the ID of the user
     * @return an object containing the generated token, its expiration time, and the refresh token
     */
    public IssueToken issueToken(Long userId) {
        UserEntity userEntity = userRepository.findById(userId).orElse(null);
        return getIssueToken(userEntity);
    }

    /**
     * Generates an issue token for the given email.
     *
     * @param email the email of the user
     * @return an IssueToken object containing the access token, access token expiration, and
     * refresh token
     */
    public IssueToken issueToken(String email) {
        UserEntity userEntity = userRepository.findByEmail(email).orElse(null);
        return getIssueToken(userEntity);
    }

    @NotNull
    private IssueToken getIssueToken(UserEntity userEntity) {
        if (userEntity == null) {
            throw new AccessDeniedException("error.accessDenied");
        }

        AccessTokenEntity accessToken = createAccessToken(userEntity);
        RefreshTokenEntity refreshToken = createRefreshToken(accessToken);

        return new IssueToken(
            JwtProvider.BEARER,
            jwtProvider.accessTokenKey(),
            accessToken.getToken(),
            jwtProvider.accessTokenExpiration(),
            jwtProvider.refreshTokenKey(),
            refreshToken.getToken()
        );
    }

    /**
     * Creates an access token for the given user entity.
     *
     * @param userEntity the user entity for which the access token is created
     * @return the created access token entity
     */
    private AccessTokenEntity createAccessToken(UserEntity userEntity) {
        String token = jwtProvider.createAccessToken(userEntity.getEmail());

        var accessToken = AccessTokenEntity.builder()
            .type(JwtProvider.BEARER)
            .token(token)
            .expiration(jwtProvider.accessTokenExpiration())
            .userId(userEntity.getId())
            .build();

        return accessTokenRepository.save(accessToken);
    }

    /**
     * Create a new refresh token based on the given access token.
     *
     * @param tokenEntity the access token entity
     * @return the newly created refresh token entity
     */
    private RefreshTokenEntity createRefreshToken(AccessTokenEntity tokenEntity) {
        String token = jwtProvider.createRefreshToken();
        Date expiresAt = jwtProvider.extractExpiresAt(token).orElse(new Date());
        LocalDateTime exp = expiresAt.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();

        var refreshToken = RefreshTokenEntity.builder()
            .type(JwtProvider.BEARER)
            .token(token)
            .expiration(jwtProvider.refreshTokenExpiration())
            .accessTokenId(tokenEntity.getId())
            .build();

        return refreshTokenRepository.save(refreshToken);
    }

    public Optional<UserEntity> getUserByAccessToken(String accessToken) {
        AccessTokenEntity accessTokenEntity =
            accessTokenRepository.findByToken(accessToken).orElse(null);
        if (accessTokenEntity == null) {
            return Optional.empty();
        }

        var userId = Long.parseLong(accessTokenEntity.getUserId());

        return userRepository.findById(userId);
    }

    /**
     * Retrieves the user entity associated with the given refresh token.
     *
     * @param refreshToken the refresh token used to retrieve the user entity
     * @return the user entity associated with the refresh token
     */
    public Optional<UserEntity> getUserByRefreshToken(String refreshToken) {
        RefreshTokenEntity refreshTokenEntity =
            refreshTokenRepository.findByToken(refreshToken).orElse(null);

        if (refreshTokenEntity == null) {
            return Optional.empty();
        }

        var accessTokenEntity = accessTokenRepository
            .findById(refreshTokenEntity.getAccessTokenId()).orElse(null);
        if (accessTokenEntity == null) {
            return Optional.empty();
        }

        var userId = Long.parseLong(accessTokenEntity.getUserId());

        return userRepository.findById(userId);
    }

    /**
     * Extracts the access token from the provided HTTP servlet request.
     *
     * @param request the HTTP servlet request object from which the access token is to be
     *                extracted
     * @return an optional string containing the access token, or empty if it could not be extracted
     */
    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return jwtProvider.extractAccessToken(request);
    }

    /**
     * A description of the entire Java function.
     *
     * @param accessToken description of parameter
     * @return description of return value
     */
    public Optional<String> extractEmail(String accessToken) {
        return jwtProvider.extractEmail(accessToken);
    }

    /**
     * Checks if the given token is valid.
     *
     * @param token the token to be checked
     * @return true if the token is valid, false otherwise
     */
    public boolean isTokenValid(String token) {
        return jwtProvider.isTokenValid(token);
    }

    public void revokeTokenByUserId(Long userId) {
        var accessToken = accessTokenRepository.findByUserId(userId.toString())
            .orElseThrow(() -> new RestErrorException(
                "error.invalidToken",
                AuthErrorCode.INVALID_TOKEN
            )).getId();

        var refreshToken = refreshTokenRepository.findByAccessTokenId(accessToken)
            .orElseThrow(() -> new RestErrorException(
                "error.invalidToken",
                AuthErrorCode.INVALID_TOKEN
            )).getId();

        accessTokenRepository.deleteById(accessToken);
        refreshTokenRepository.deleteById(refreshToken);
    }
}
