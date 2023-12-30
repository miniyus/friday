package com.miniyus.friday.infrastructure.config;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * JWT Configuration
 *
 * @author miniyus
 * @since 2023/09/04
 */
@Data
@Configuration
@JsonIgnoreProperties({"secret"})
@ConfigurationProperties(prefix = "jwt")
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
