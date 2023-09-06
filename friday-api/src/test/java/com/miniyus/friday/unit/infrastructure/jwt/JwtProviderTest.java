package com.miniyus.friday.unit.infrastructure.jwt;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Date;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miniyus.friday.infrastructure.jwt.JwtProvider;

/**
 * Jwt Provider Test
 * 
 * @author miniyus
 * @date 2023/09/04
 */
@ExtendWith(MockitoExtension.class)
public class JwtProviderTest {

    private JwtProvider jwtProvider;

    private String token;

    /**
     * Set up the JWT provider for testing.
     *
     * @BeforeEach
     * @void
     */
    @BeforeEach
    void setJwtProvider() {
        jwtProvider = new JwtProvider(
                "test-secret",
                3600L,
                86400L,
                "Authorization",
                "RefreshToken");
        token = jwtProvider.createAccessToken("miniyus@gmail.com");
    }

    @Test
    void createAccessTokenTest() {
        String testEmail = "miniyus@gmail.com";
        token = jwtProvider.createAccessToken(testEmail);

        assertNotNull(token, "failed to create access token");
        assertTrue(jwtProvider.isTokenValid(token), "failed to validate access token");
    }

    /**
     * Test case for the createRefreshToken method.
     *
     * @return void
     */
    @Test
    void createRefreshTokenTest() {
        token = jwtProvider.createRefreshToken();
        assertNotNull(token, "failed to create refresh token");
        assertTrue(jwtProvider.isTokenValid(token), "failed to validate refresh token");
    }

    /**
     * Test case for the `isTokenValid` function.
     *
     * @param paramName description of parameter
     * @return description of return value
     */
    @Test
    void isTokenValidTest() {
        token = "I'm a Token";
        assertFalse(jwtProvider.isTokenValid(token), "failed to validate invalid token, this is not a token");
    }

    /**
     * Test case for the `extractTokenExpires` function.
     *
     * This test case verifies that the `extractTokenExpires` function correctly
     * extracts the expiration date from a JWT token and ensures that the
     * extracted date is not null and is in the future.
     *
     * @param None
     * @return None
     */
    @Test
    void extractTokenExpiresTest() {
        Date expDate = jwtProvider.extractExpiresAt(token).get();
        assertNotNull(expDate);
        assertTrue(expDate.getTime() > new Date().getTime());
    }

    /**
     * Test case for the extractEmail method.
     *
     * This test case verifies the behavior of the extractEmail method.
     * It checks if the extracted email from the token matches the expected email.
     */
    @Test
    void extractEmailTest() {
        String email = jwtProvider.extractEmail(token).get();
        assertNotNull(email);
        assertEquals("miniyus@gmail.com", email);
    }
}
