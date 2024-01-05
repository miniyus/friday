package com.meteormin.friday;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.GenericContainer;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@Testcontainers
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public abstract class AbstractTestContainerTest {
    private static final int REDIS_PORT = 6379;

    private static final String REDIS_IMAGE = "redis:latest";

    @Autowired
    private Environment environment;

    private static final GenericContainer<?> REDIS_CONTAINER;

    static {
        REDIS_CONTAINER =
            new GenericContainer<>(DockerImageName.parse(REDIS_IMAGE))
                .withExposedPorts(REDIS_PORT);
        REDIS_CONTAINER.start();
    }

    @DynamicPropertySource
    public static void overrideProps(DynamicPropertyRegistry registry) {
        registry.add("spring.data.redis.host", REDIS_CONTAINER::getHost);
        registry.add("spring.data.redis.port",
            () -> String.valueOf(REDIS_CONTAINER.getMappedPort(REDIS_PORT))
        );
    }
}
