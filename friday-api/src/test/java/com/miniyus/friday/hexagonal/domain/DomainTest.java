package com.miniyus.friday.hexagonal.domain;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import com.precisionbio.cuttysark.common.fake.FakeInjector;
import org.junit.jupiter.api.BeforeEach;

public abstract class DomainTest {
    protected final Faker faker = new Faker();
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected FakeInjector fakeInjector;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        fakeInjector = new FakeInjector(new Faker(), objectMapper);
    }
}
