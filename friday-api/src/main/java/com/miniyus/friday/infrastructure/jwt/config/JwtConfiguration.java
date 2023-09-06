package com.miniyus.friday.infrastructure.jwt.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import lombok.Data;

/**
 * JWT Configuration
 *
 * @author miniyus
 * @date 2023/09/04
 */
@Configuration
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfiguration {
    private String secret;

    private AccessConfiguration access;

    private RefreshConfiguration refresh;

    @Bean
    public JwtProvider jwtProvider() {
        return new JwtProvider(
                secret,
                access.getExpiration(),
                refresh.getExpiration(),
                access.getHeader(),
                refresh.getHeader());
    }
}
