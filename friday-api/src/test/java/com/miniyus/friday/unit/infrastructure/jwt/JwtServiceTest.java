package com.miniyus.friday.unit.infrastructure.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.util.Optional;
import com.miniyus.friday.infrastructure.persistence.entities.AccessTokenEntity;
import com.miniyus.friday.infrastructure.persistence.entities.RefreshTokenEntity;
import com.miniyus.friday.common.UserRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.AccessTokenEntityRepository;
import com.miniyus.friday.infrastructure.persistence.repositories.RefreshTokenEntityRepository;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;

/**
 * JWT Service Test
 *
 * @author miniyus
 * @date 2023/09/06
 */
@ExtendWith(MockitoExtension.class)
public class JwtServiceTest {

    @Mock
    private UserEntityRepository userRepository;

    @Mock
    private AccessTokenEntityRepository accessTokenRepository;

    @Mock
    private RefreshTokenEntityRepository refreshTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private JwtService jwtService;

    private UserEntity testUser;

    /**
     * Set up the test environment before each test case.
     *
     */
    @BeforeEach
    void setUp() {
        // jwt provider dependency injection is not auto-injected to @Mockannotaion,
        // so manual injection
        jwtProvider = new JwtProvider(
            "test-secret",
            3600L,
            86400L,
            "Authorization",
            "RefreshToken");

        jwtService = new JwtService(
            jwtProvider,
            userRepository,
            accessTokenRepository,
            refreshTokenRepository);

        testUser = UserEntity.builder()
            .name("miniyus")
            .password("test@password")
            .email("miniyus@gmail.com")
            .role(UserRole.USER)
            .build();
    }

    /**
     * A test for the issueToken method.
     *
     */
    @Test
    void issueTokenTest() {
        // If user repository calls findByEmail, specify the return value as arbitrary test data
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(new UserEntity(
            1L,
            null,
            null,
            testUser.getEmail(),
            testUser.getPassword(),
            testUser.getName(),
            testUser.getRole(),
            testUser.getDeletedAt()
        )));

        var testAccessToken = AccessTokenEntity.builder()
            .token(jwtProvider.createAccessToken(testUser.getEmail()))
            .expiration(3600L)
            .type(JwtProvider.BEARER)
            .userId(1L)
            .build();

        when(accessTokenRepository.save(any())).thenReturn(
            new AccessTokenEntity(
                "1",
                JwtProvider.BEARER,
                testAccessToken.getToken(),
                testAccessToken.getUserId(),
                testAccessToken.getExpiration()
            )
        );

        var testRefreshToken = RefreshTokenEntity.builder()
            .token(jwtProvider.createRefreshToken())
            .expiration(3600L)
            .type(JwtProvider.BEARER)
            .accessTokenId("1")
            .build();

        when(refreshTokenRepository.save(any())).thenReturn(
            new RefreshTokenEntity(
                "1",
                JwtProvider.BEARER,
                testRefreshToken.getToken(),
                testRefreshToken.getAccessTokenId(),
                testRefreshToken.getExpiration()
            )
        );

        var tokens = jwtService.issueToken(testUser.getEmail());
        var accessToken = tokens.accessToken();
        var refreshToken = tokens.refreshToken();

        // just null check
        assertThat(tokens)
            .isNotNull()
            .hasFieldOrProperty("tokenType")
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("expiresIn")
            .hasFieldOrProperty("refreshToken");

        // jwt token validations
        assertThat(jwtProvider.isTokenValid(accessToken)).isTrue();
        assertThat(jwtProvider.isTokenValid(refreshToken)).isTrue();
        var extractEmail = jwtProvider.extractEmail(accessToken).orElse(null);
        // check extracted email from access token
        assertThat(extractEmail)
            .isNotNull()
            .isEqualTo(testUser.getEmail());

        /*
          Verify that the expiration date of the access token.
          * check injected into the JWT Provider matches the expiration date of the issued access token
         */
        assertThat(jwtProvider.extractExpiresAt(accessToken))
            .isNotNull()
            .isEqualTo(jwtProvider.extractExpiresAt(testAccessToken.getToken()));
    }
}
