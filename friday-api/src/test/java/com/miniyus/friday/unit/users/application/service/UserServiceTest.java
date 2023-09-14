package com.miniyus.friday.unit.users.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import com.github.javafaker.Faker;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserCommand;
import com.miniyus.friday.users.application.port.out.CreateUserPort;
import com.miniyus.friday.users.application.port.out.DeleteUserPort;
import com.miniyus.friday.users.application.port.out.RetrieveUserPort;
import com.miniyus.friday.users.application.port.out.UpdateUserPort;
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

    @Mock
    private RetrieveUserPort retrieveUserPort;

    @Mock
    private UpdateUserPort updateUserPort;

    @Mock
    private DeleteUserPort deleteUserPort;

    @InjectMocks
    private UserService userService;

    private List<User> testDomains;

    private final Faker faker = new Faker();

    @BeforeEach
    void setUp() {
        testDomains = new ArrayList<User>();
        for (int i = 0; i < 10; i++) {
            var testDomain = new User(
                    i + 1L,
                    faker.internet().safeEmailAddress(),
                    faker.internet().password(),
                    faker.name().fullName(),
                    "USER",
                    null,
                    null,
                    LocalDateTime.now(),
                    LocalDateTime.now(),
                    null);
            testDomains.add(testDomain);
        }
    }

    @Test
    void createUser() {
        var testDomain = testDomains.get(0);

        CreateUserCommand createUserCommand = CreateUserCommand.builder()
            .email(testDomain.getEmail())
            .name(testDomain.getName())
            .role("USER")
            .password(testDomain.getPassword())
            .build();

        when(createUserPort.createUser(any())).thenReturn(testDomain);

        User created = userService.createUser(createUserCommand);

        assertThat(created)
                .hasFieldOrPropertyWithValue("email", createUserCommand.email())
                .hasFieldOrPropertyWithValue("name", createUserCommand.name())
                .hasFieldOrPropertyWithValue("role", createUserCommand.role())
                .hasFieldOrPropertyWithValue("password", createUserCommand.password());
    }

    @Test
    void retrieveUser() throws Exception {
        var testDomain = testDomains.get(0);
        when(retrieveUserPort.findById(1L)).thenReturn(testDomain);

        User retrieved = userService.findById(1L);

        assertThat(retrieved)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", 1L)
                .hasFieldOrPropertyWithValue("email", testDomain.getEmail())
                .hasFieldOrPropertyWithValue("name", testDomain.getName())
                .hasFieldOrPropertyWithValue("role", testDomain.getRole())
                .hasFieldOrPropertyWithValue("password", testDomain.getPassword());
    }

    @Test
    void retrieveUsers() throws Exception {
        when(retrieveUserPort.findAll()).thenReturn(testDomains);

        Collection<User> retrieved = userService.findAll();

        assertThat(retrieved.size())
            .isNotEqualTo(0)
            .isEqualTo(testDomains.size());
    }

    @Test
    void updateUser() throws Exception {
        var testDomain = testDomains.get(0);
        when(updateUserPort.findById(1L)).thenReturn(testDomain);

        var testOrigin = testDomains.get(0);

        var testUpdate = new User(
                testOrigin.getId(),
                testOrigin.getEmail(),
                testOrigin.getPassword(),
                testOrigin.getName(),
                testOrigin.getRole(),
                testOrigin.getSnsId(),
                testOrigin.getProvider(),
                testOrigin.getCreatedAt(),
                testOrigin.getUpdatedAt(),
                testOrigin.getDeletedAt());

        testUpdate.patch("testUpdate", null);

        when(updateUserPort.updateUser(any(User.class))).thenReturn(testUpdate);
        var command = UpdateUserCommand.builder()
                .id(1L)
                .name("testUpdate")
                .role("USER")
                .build();

        User updated = userService.patchUser(command);

        assertThat(updated)
                .hasFieldOrPropertyWithValue("id", command.id())
                .hasFieldOrPropertyWithValue("name", command.name())
                .hasFieldOrPropertyWithValue("role", command.role());
    }

    @Test
    void deleteUser() throws Exception {
        when(retrieveUserPort.findById(1L)).thenReturn(null);

        userService.deleteById(1L);
        
        User deleted = null;
        try {
            deleted = userService.findById(1L);
        } catch (RestErrorException exception){
            assertThat(exception).isInstanceOf(RestErrorException.class);
            assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        assertThat(deleted).isNull();
    }
}
