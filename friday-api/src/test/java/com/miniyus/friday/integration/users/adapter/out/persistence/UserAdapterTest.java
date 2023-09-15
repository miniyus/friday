package com.miniyus.friday.integration.users.adapter.out.persistence;

import static org.assertj.core.api.Assertions.assertThat;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import org.testcontainers.junit.jupiter.Testcontainers;
import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.api.users.adapter.out.persistence.UserAdapter;
import com.miniyus.friday.api.users.adapter.out.persistence.UserMapper;
import com.miniyus.friday.api.users.domain.User;

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
    private UserRepository userRepository;

    private Faker faker = new Faker();

    private List<UserEntity> testEntities = new ArrayList<UserEntity>();

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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
            testEntities.add(UserMapper.toEntity(testDomain));
        }

        testEntities = userRepository.saveAll(testEntities);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @Order(100)
    public void retrieveUsers() {
        Collection<User> users = userAdapter.findAll();

        assertThat(users).isNotNull();
        assertThat(users.size()).isEqualTo(testEntities.size());
    }

    @Test
    @Order(200)
    public void retrieveUser() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User user = userAdapter.findById(latestUser.getId());

        var testUser = UserMapper.toDomain(latestUser);

        assertThat(user).isNotNull()
                .hasFieldOrPropertyWithValue("id", testUser.getId());
    }

    @Test
    @Order(300)
    public void createUser() {
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
    public void updateUser() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User origin = userAdapter.findById(latestUser.getId());
        origin.patch("updateName", null);
        User updated = userAdapter.updateUser(origin);

        assertThat(updated).isNotNull()
                .hasFieldOrPropertyWithValue("id", origin.getId())
                .hasFieldOrPropertyWithValue("name", "updateName");
    }

    @Test
    @Order(500)
    public void deleteUser() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User origin = userAdapter.findById(latestUser.getId());
        origin.delete();
        userAdapter.deleteById(latestUser.getId());
        User deleted = userAdapter.findById(latestUser.getId());

        assertThat(deleted).isNull();
    }

    @Test
    @Order(401)
    public void resetPassword() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User user = userAdapter.findById(latestUser.getId());
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
}
