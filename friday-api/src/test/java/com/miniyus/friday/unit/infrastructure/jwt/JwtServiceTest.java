package com.miniyus.friday.unit.infrastructure.jwt;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.Optional;
import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.jpa.entities.AccessTokenEntity;
import com.miniyus.friday.infrastructure.jpa.entities.RefreshTokenEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.AccessTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
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
    private UserRepository userRepository;

    @Mock
    private AccessTokenRepository accessTokenRepository;

    @Mock
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private JwtService jwtService;

    private UserEntity testUser;

    private Faker faker = new Faker();

    /**
     * Set up the test environment before each test case.
     *
     * @param paramName description of parameter
     * @return description of return value
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
            .role("USER")
            .build();
    }

    /**
     * A test for the issueToken method.
     *
     * @param paramName description of parameter
     * @return description of return value
     */
    @Test
    void issueTokenTest() {
        // If user repository calls findByEmail, specify the return value as arbitrary test data
        when(userRepository.findByEmail(testUser.getEmail())).thenReturn(Optional.of(testUser));

        var testAccessToken = AccessTokenEntity.builder()
            .token(faker.internet().uuid())
            .expiration(3600L)
            .expiresAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .type(JwtProvider.BEARER)
            .userId(testUser.getId())
            .build();

        when(accessTokenRepository.save(any())).thenReturn(
            new AccessTokenEntity(
                1L,
                JwtProvider.BEARER,
                testAccessToken.getToken(),
                testAccessToken.getExpiresAt(),
                testAccessToken.getExpiration(),
                testAccessToken.getUserId()
            )
        );

        var testRefreshToken = RefreshTokenEntity.builder()
            .token(faker.internet().uuid())
            .expiration(3600L)
            .expiresAt(LocalDateTime.of(2024, 1, 1, 0, 0))
            .type(JwtProvider.BEARER)
            .accessTokenId(testAccessToken.getId())
            .build();

        when(refreshTokenRepository.save(any())).thenReturn(
            new RefreshTokenEntity(
                1L,
                JwtProvider.BEARER,
                testRefreshToken.getToken(),
                testRefreshToken.getExpiresAt(),
                testRefreshToken.getExpiration(),
                testRefreshToken.getAccessTokenId()
            )
        );

        var tokens = jwtService.issueToken(testUser.getEmail());
        var accessToken = tokens.accessToken();
        var refreshToken = tokens.refreshToken();

        // just null check
        assertThat(tokens)
            .isNotNull()
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("expiresIn")
            .hasFieldOrProperty("refreshToken");

        // jwt token validations
        //        assertTrue(jwtProvider.isTokenValid(accessToken), "failed to validate access token");
        //        assertTrue(jwtProvider.isTokenValid(refreshToken), "failed to validate refresh token");
        //        var extractEmail = jwtProvider.extractEmail(accessToken).orElse(null);
        // check extracted email from access token
        //        assertEquals(testUser.getEmail(), extractEmail);

        /**
         * Verify that the expiration date of the access token. 
         * * check injected into the JWT Provider matches the expiration date of the issued access token
         */
//        assertEquals(3600L, (long) tokens.expiresIn());
    }
}
