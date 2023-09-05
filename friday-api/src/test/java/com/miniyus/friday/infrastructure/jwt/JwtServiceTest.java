package com.miniyus.friday.infrastructure.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.when;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;

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
    private RefreshTokenRepository refreshTokenRepository;

    @Mock
    private JwtProvider jwtProvider;

    @InjectMocks
    private JwtService jwtService;

    private UserEntity testUser;

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

        jwtService = new JwtService(jwtProvider, userRepository, refreshTokenRepository);

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
     * @param  paramName	description of parameter
     * @return         	description of return value
     */
    @Test
    void issueTokenTest() {
        // If user repository calls findByEmail, specify the return value as arbitrary test data
        when(userRepository.findByEmail("miniyus@gmail.com")).thenReturn(Optional.of(testUser));
        
        var tokens = jwtService.issueToken("miniyus@gmail.com");
        var accessToken = tokens.getAccessToken();
        var refreshToken = tokens.getRefreshToken();

        // just null check
        assertNotNull(tokens);

        // jwt token validations
        assertTrue(jwtProvider.isTokenValid(accessToken), "failed to validate access token");
        assertTrue(jwtProvider.isTokenValid(refreshToken), "failed to validate refresh token");
        
        /**
         * Verify that the expiration date of the access token. 
         * * check injected into the JWT Provider matches the expiration date of the issued access token
         */
        assertTrue(tokens.getExpiresIn() == 3600L);

        var email = jwtProvider.extractEmail(accessToken).get();
        
        // check extracted email from access token
        assertEquals(testUser.getEmail(), email);
    }
}
