package com.miniyus.friday.integration.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.miniyus.friday.users.domain.UserFilter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.users.adapter.out.persistence.UserAdapter;
import com.miniyus.friday.users.adapter.out.persistence.mapper.UserMapper;
import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/10
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
public class UserAdapterTest {

    @Autowired
    private UserAdapter userAdapter;

    @Autowired
    private UserEntityRepository userRepository;

    private final Faker faker = new Faker();

    private List<UserEntity> testEntities = new ArrayList<>();

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final UserMapper userMapper = new UserMapper();

    @BeforeEach
    public void setup() {
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
            testEntities.add(userMapper.toEntity(testDomain));
        }

        testEntities = userRepository.saveAll(testEntities);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @Order(100)
    public void retrieveUsersTest() {
        Collection<User> users = userAdapter.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(testEntities.size());
    }

    @Test
    @Order(200)
    public void retrieveUserTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User user = userAdapter.findById(latestUser.getId()).orElse(null);

        var testUser = userMapper.toDomain(latestUser);

        assertThat(user).isNotNull()
            .hasFieldOrPropertyWithValue("id", testUser.getId());
    }

    @Test
    @Order(300)
    public void createUserTest() {
        User created = userAdapter.createUser(
            new User(
                null,
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                faker.name().fullName(),
                "USER",
                null,
                null,
                null,
                null,
                null));

        assertThat(created).isNotNull()
            .hasFieldOrProperty("id");
        assertThat(created.getId()).isNotNull();
    }

    @Test
    @Order(400)
    public void updateUserTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User origin = userAdapter.findById(latestUser.getId()).orElse(null);

        assert origin != null;

        origin.patch("updateName", null);
        User updated = userAdapter.updateUser(origin);

        assertThat(updated).isNotNull()
            .hasFieldOrPropertyWithValue("id", origin.getId())
            .hasFieldOrPropertyWithValue("name", "updateName");
    }

    @Test
    @Order(500)
    public void deleteUserTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User origin = userAdapter.findById(latestUser.getId()).orElse(null);

        assert origin != null;

        origin.delete();
        userAdapter.deleteById(latestUser.getId());
        User deleted = userAdapter.findById(latestUser.getId()).orElse(null);

        assertThat(deleted).isNull();
    }

    @Test
    @Order(401)
    public void resetPasswordTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User user = userAdapter.findById(latestUser.getId()).orElse(null);

        assert user != null;

        user.resetPassword("resetPassword");
        User updated = userAdapter.resetPassword(user);

        var equalPassword = passwordEncoder.matches("resetPassword", updated.getPassword());

        // Because encryption is done by the service layer
        assertThat(equalPassword).isFalse();
        // Therefore, the password entered must be the same as the actual saved password.
        // why? Because the adapter is the infrastructure (database) layer.
        assertThat(updated.getPassword())
            .isEqualTo(user.getPassword());
    }

    Pageable getPageable() {
        return PageRequest.of(
            0,
            20,
            Sort.by(Sort.Direction.DESC, "created_at")
        );
    }

    @Test
    void retrieveFilterByEmailTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .email(selectUser.getEmail())
            .build();

        var users = userAdapter.findAll(
            filter,
            getPageable()
        );

        assertThat(users).isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void retrieveFilterByNameTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .name(selectUser.getName())
            .build();

        var users = userAdapter.findAll(
            filter,
            getPageable()
        );

        assertThat(users).isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void retrieveFilterRangeCreatedAtTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .createdAtStart(selectUser.getCreatedAt())
            .createdAtEnd(testEntities.get(testEntities.size() - 1).getCreatedAt())
            .build();

        var users = userAdapter.findAll(
            filter,
            getPageable()
        );

        assertThat(users).isNotNull()
            .isNotEmpty();
    }

    @Test
    void retrieveFilterRangeUpdatedAtTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .updatedAtStart(selectUser.getUpdatedAt())
            .updatedAtEnd(testEntities.get(testEntities.size() - 1).getUpdatedAt())
            .build();

        var users = userAdapter.findAll(
            filter,
            getPageable()
        );

        assertThat(users).isNotNull()
            .isNotEmpty();
    }

    @Test
    void retrieveFilterMultipleTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .email(selectUser.getEmail())
            .name(selectUser.getName())
            .updatedAtStart(testEntities.get(0).getUpdatedAt())
            .updatedAtEnd(testEntities.get(testEntities.size() - 1).getUpdatedAt())
            .build();

        var users = userAdapter.findAll(
            filter,
            getPageable()
        );

        assertThat(users).isNotNull()
            .isNotEmpty();
    }
}
