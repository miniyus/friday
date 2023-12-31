package com.meteormin.friday.hexagonal.application;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meteormin.friday.common.fake.FakeInjector;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public abstract class UsecaseTest {
    protected final Faker faker = new Faker();
    protected final ObjectMapper objectMapper = new ObjectMapper();
    protected FakeInjector fakeInjector;

    @BeforeEach
    public void setUp() {
        objectMapper.registerModule(new JavaTimeModule());
        fakeInjector = new FakeInjector(new Faker(), objectMapper);
    }
}
