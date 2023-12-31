package com.meteormin.friday.users.application;

import com.meteormin.friday.api.users.request.CreateUserRequest;
import com.meteormin.friday.api.users.request.UpdateUserRequest;
import com.meteormin.friday.common.error.RestErrorException;
import com.meteormin.friday.hexagonal.application.UsecaseTest;
import com.meteormin.friday.users.application.port.out.UserPort;
import com.meteormin.friday.users.application.service.UserService;
import com.meteormin.friday.users.domain.PatchUser;
import com.meteormin.friday.users.domain.User;
import com.meteormin.friday.users.domain.UserRole;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.openapitools.jackson.nullable.JsonNullable;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * Create User Service
 *
 * @author meteormin
 * @date 2023/09/06
 */
class UserServiceTest extends UsecaseTest {
    @Mock
    private UserPort userPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private List<User> testDomains;

    private final Faker faker = new Faker();

    @BeforeEach
    public void setUp() {
        super.setUp();
        testDomains = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            var testDomain = new User(
                i + 1L,
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                faker.name().fullName(),
                UserRole.USER,
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

        CreateUserRequest createUser = CreateUserRequest.builder()
            .email(testDomain.getEmail())
            .name(testDomain.getName())
            .role("USER")
            .password(testDomain.getPassword())
            .build();

        when(userPort.createUser(any())).thenReturn(testDomain);

        User created = userService.createUser(createUser.toDomain());

        assertThat(created)
            .hasFieldOrPropertyWithValue("email", createUser.email())
            .hasFieldOrPropertyWithValue("name", createUser.name())
            .hasFieldOrPropertyWithValue("role", UserRole.USER);
    }

    @Test
    void retrieveUser() throws Exception {
        var testDomain = testDomains.get(0);
        when(userPort.findById(1L)).thenReturn(Optional.of(testDomain));

        User retrieved = userService.findById(1L);

        assertThat(retrieved)
            .isNotNull()
            .hasFieldOrPropertyWithValue("id", 1L)
            .hasFieldOrPropertyWithValue("email", testDomain.getEmail())
            .hasFieldOrPropertyWithValue("name", testDomain.getName())
            .hasFieldOrPropertyWithValue("role", testDomain.getRole());
    }

    @Test
    void retrieveUsers() throws Exception {
        when(userPort.findAll()).thenReturn(testDomains);

        List<User> retrieved = userService.findAll();

        assertThat(retrieved.size())
            .isNotEqualTo(0)
            .isEqualTo(testDomains.size());
    }

    @Test
    void updateUser() throws Exception {
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

        testUpdate.patch(PatchUser.builder()
            .id(testOrigin.getId())
            .name(JsonNullable.of("testUpdate"))
            .role(JsonNullable.of(UserRole.USER))
            .build());

        when(userPort.findById(any())).thenReturn(Optional.of(testOrigin));
        when(userPort.updateUser(any(User.class))).thenReturn(testUpdate);

        var request = UpdateUserRequest.builder()
            .name(JsonNullable.of("testUpdate"))
            .role(JsonNullable.of("USER"))
            .build();

        User updated = userService.patchUser(request.toDomain(testOrigin.getId()));

        assertThat(updated)
            .hasFieldOrPropertyWithValue("id", testOrigin.getId())
            .hasFieldOrPropertyWithValue("name", request.name().get())
            .hasFieldOrPropertyWithValue("role", UserRole.USER);
    }

    @Test
    void deleteUser() throws Exception {
        when(userPort.findById(any())).thenReturn(Optional.empty());

        userService.deleteUserById(1L);

        User deleted = null;
        try {
            deleted = userService.findById(1L);
        } catch (RestErrorException exception) {
            assertThat(exception).isInstanceOf(RestErrorException.class);
            assertThat(exception.getErrorCode().getHttpStatus()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        assertThat(deleted).isNull();
    }
}
