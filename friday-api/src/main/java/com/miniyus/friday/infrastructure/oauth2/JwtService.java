package com.miniyus.friday.infrastructure.oauth2;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.AccessTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.infrastructure.oauth2.jwt.IssueTokenResponse;
import com.miniyus.friday.infrastructure.oauth2.jwt.JwtProvider;

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
    private final AccessTokenRepository accessTokenRepository;
    private final RefreshTokenRepository refreshTokenRepository;

    public IssueTokenResponse issueToken(PrincipalUserInfo userInfo) {
        AccessTokenEntity accessToken = createAccessToken(userInfo);
        return new IssueTokenResponse(
                userInfo.getSnsId(),
                userInfo.getProvider().getId(),
                accessToken.getToken(),
                jwtProvider.getAccessTokenExpiration(),
                accessToken.getRefreshToken().getToken());
    }

    private AccessTokenEntity createAccessToken(PrincipalUserInfo userInfo) {
        String token = jwtProvider.createAccessToken(userInfo.getUsername());
        UserEntity userEntity = userRepository.findById(userInfo.getId()).get();

        if (userEntity == null) {
            throw new AccessDeniedException("Cannot find user");
        }

        Date expiresAt = jwtProvider.extractExpiresAt(token).get();
        LocalDateTime exp = expiresAt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        AccessTokenEntity tokenEntity = new AccessTokenEntity(
                null,
                "Bearer",
                token,
                exp,
                false,
                null,
                null,
                null,
                null);

        tokenEntity.setUser(userEntity);

        RefreshTokenEntity refreshToken = createRefreshToken(tokenEntity);

        tokenEntity.setRefreshToken(refreshToken);

        return saveAccessToken(tokenEntity);
    }

    private RefreshTokenEntity createRefreshToken(AccessTokenEntity tokenEntity) {
        String refreshToken = jwtProvider.createRefreshToken();
        Date expiresAt = jwtProvider.extractExpiresAt(refreshToken).get();
        LocalDateTime exp = expiresAt.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        RefreshTokenEntity refreshTokenEntity = new RefreshTokenEntity(
                null,
                "Bearer",
                refreshToken,
                exp,
                false,
                null,
                null,
                null);

        return refreshTokenEntity;
    }

    private AccessTokenEntity saveAccessToken(AccessTokenEntity entity) {
        return accessTokenRepository.save(entity);
    }

    private RefreshTokenEntity saveRefreshToken(RefreshTokenEntity enttiy) {
        return refreshTokenRepository.save(enttiy);
    }
}
