package com.miniyus.friday.infrastructure.config;

import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration
 *
 * @author miniyus
 * @date 2023/09/04
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@RequiredArgsConstructor
@Data
public class JwtConfiguration {
    private String secret;

    private TokenConfiguration accessToken;

    private TokenConfiguration refreshToken;

    public record TokenConfiguration(
        Long expiration,
        String header
    ) {
    }

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(
            secret,
            accessToken.expiration(),
            refreshToken.expiration(),
            accessToken.header(),
            refreshToken.header());
    }
}
