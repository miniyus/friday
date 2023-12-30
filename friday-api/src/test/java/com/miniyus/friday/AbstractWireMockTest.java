package com.miniyus.friday;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miniyus.friday.common.fake.FakeInjector;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.cloud.contract.wiremock.AutoConfigureWireMock;
import org.springframework.cloud.openfeign.FeignAutoConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureWireMock(port = 0)
@ImportAutoConfiguration({FeignAutoConfiguration.class})
@TestPropertySource(properties = {
    "spring.cloud.openfeign.client.config.dms.url=http://localhost:${wiremock.server.port}",
    "app.secret=secret",
})
public abstract class AbstractWireMockTest extends AbstractTestContainerTest {
    @Autowired
    protected ObjectMapper objectMapper;

    protected FakeInjector fakeInjector;

    @BeforeEach
    public void setup() {
        objectMapper.registerModule(new JavaTimeModule());
        fakeInjector = new FakeInjector(new Faker(), objectMapper);
    }
}
