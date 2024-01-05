package com.meteormin.friday.infrastructure.config;

import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

@Configuration
@EnableCaching
public class RedisCacheConfiguration {
    @Bean
    public org.springframework.data.redis.cache.RedisCacheConfiguration cacheConfiguration() {
        return org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(60))
            .disableCachingNullValues()
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(
                    new GenericJackson2JsonRedisSerializer())
            );
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer() {
        return builder -> builder
            .withCacheConfiguration(
                "queue",
                org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
            )
            .withCacheConfiguration(
                "cache",
                org.springframework.data.redis.cache.RedisCacheConfiguration.defaultCacheConfig().entryTtl(Duration.ofMinutes(10))
            );
    }
}
