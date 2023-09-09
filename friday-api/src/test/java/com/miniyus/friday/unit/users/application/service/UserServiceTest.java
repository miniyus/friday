package com.miniyus.friday.unit.users.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.out.CreateUserPort;
import com.miniyus.friday.users.application.service.UserService;
import com.miniyus.friday.users.domain.User;

/**
 * Create User Service
 *
 * @author miniyus
 * @date 2023/09/06
 */
@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @Mock
    private CreateUserPort createUserPort;

    @InjectMocks
    private UserService createUserService;

    private CreateUserCommand createUserCommand;

    private User testDomain;

    @BeforeEach
    void setUp() {
        testDomain = new User(
                1L,
                "miniyus@gmail.com",
                "password@1234",
                "smyoo",
                "USER",
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);
    }

    @Test
    void createUser() {
        createUserCommand = CreateUserCommand.builder()
                .email("miniyu97@gmail.com")
                .name("smyoo")
                .role("USER")
                .password("password@1234")
                .build();

        when(createUserPort.createUser(any())).thenReturn(testDomain);

        User created = createUserService.createUser(createUserCommand);

        assertEquals(testDomain, created);
    }
}
