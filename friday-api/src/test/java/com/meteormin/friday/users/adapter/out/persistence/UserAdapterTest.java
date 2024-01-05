package com.meteormin.friday.users.adapter.out.persistence;

import com.meteormin.friday.hexagonal.adapter.PersistenceTest;
import com.meteormin.friday.infrastructure.persistence.entities.UserEntity;
import com.meteormin.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.meteormin.friday.users.adapter.out.persistence.mapper.UserMapper;
import com.meteormin.friday.users.domain.PatchUser;
import com.meteormin.friday.users.domain.User;
import com.meteormin.friday.users.domain.UserFilter;
import com.meteormin.friday.users.domain.UserRole;
import net.datafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.openapitools.jackson.nullable.JsonNullable;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * [description]
 *
 * @author meteormin
 * @date 2023/09/10
 */
@SpringBootTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
@Testcontainers
@Transactional
class UserAdapterTest extends PersistenceTest<Long, UserEntity> {

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
                UserRole.USER,
                null,
                null,
                LocalDateTime.now(),
                LocalDateTime.now(),
                null);
            testEntities.add(userMapper.toUserEntity(testDomain));
        }

        testEntities = userRepository.saveAll(testEntities);
    }

    @AfterEach
    public void tearDown() {
        userRepository.deleteAll();
    }


    @Test
    @Order(100)
    void retrieveUsersTest() {
        Collection<User> users = userAdapter.findAll();
        assertThat(users)
            .isNotNull()
            .hasSameSizeAs(testEntities);
    }

    @Test
    @Order(200)
    void retrieveUserTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User user = userAdapter.findById(latestUser.getId()).orElse(null);

        var testUser = userMapper.toUserDomain(latestUser);

        assertThat(user).isNotNull()
            .hasFieldOrPropertyWithValue("id", testUser.getId());
    }

    @Test
    @Order(300)
    void createUserTest() {
        User created = userAdapter.createUser(
            new User(
                null,
                faker.internet().safeEmailAddress(),
                faker.internet().password(),
                faker.name().fullName(),
                UserRole.USER,
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
    void updateUserTest() {
        var latestUser = testEntities.get(testEntities.size() - 1);
        User origin = userAdapter.findById(latestUser.getId()).orElse(null);

        assert origin != null;

        origin.patch(PatchUser.builder()
            .name(JsonNullable.of("updateName"))
            .role(JsonNullable.of(UserRole.USER))
            .build());
        User updated = userAdapter.updateUser(origin);

        assertThat(updated).isNotNull()
            .hasFieldOrPropertyWithValue("id", origin.getId())
            .hasFieldOrPropertyWithValue("name", "updateName");
    }

    @Test
    @Order(500)
    void deleteUserTest() {
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
    void resetPasswordTest() {
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
            .pageable(PageRequest.of(0, 20))
            .build();

        var users = userAdapter.findAll(filter);

        assertThat(users).isNotNull()
            .isNotEmpty()
            .hasSize(1);
    }

    @Test
    void retrieveFilterByNameTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .name(selectUser.getName())
            .pageable(PageRequest.of(0, 20))
            .build();

        var users = userAdapter.findAll(filter);

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
            .pageable(PageRequest.of(0, 20))
            .build();

        var users = userAdapter.findAll(filter);

        assertThat(users).isNotNull()
            .isNotEmpty();
    }

    @Test
    void retrieveFilterRangeUpdatedAtTest() {
        var selectUser = testEntities.get(5);

        var filter = UserFilter.builder()
            .updatedAtStart(selectUser.getUpdatedAt())
            .updatedAtEnd(testEntities.get(testEntities.size() - 1).getUpdatedAt())
            .pageable(PageRequest.of(0, 20))
            .build();

        var users = userAdapter.findAll(filter);

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
            .pageable(PageRequest.of(0, 20))
            .build();

        var users = userAdapter.findAll(filter);

        assertThat(users).isNotNull()
            .isNotEmpty();
    }

    @Override
    protected UserEntity createTestEntity(int index) {
        return null;
    }
}
