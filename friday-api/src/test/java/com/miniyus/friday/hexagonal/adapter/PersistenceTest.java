package com.miniyus.friday.hexagonal.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.miniyus.friday.AbstractTestContainerTest;
import com.miniyus.friday.common.fake.FakeInjector;
import com.miniyus.friday.infrastructure.persistence.BaseEntity;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.security.social.SocialProvider;
import com.miniyus.friday.users.domain.UserRole;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
@SpringBootTest
public abstract class PersistenceTest<Id, Entity extends BaseEntity<Id>> extends
    AbstractTestContainerTest {
    @Autowired
    protected UserEntityRepository userEntityRepository;

    protected final Faker faker = new Faker();

    protected final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected abstract Entity createTestEntity(int index);

    protected UserEntity testUserEntity;

    protected FakeInjector fakeInjector;

    public PersistenceTest() {
        super();
        var objetMapper = new ObjectMapper();
        objetMapper.registerModule(new JavaTimeModule());
        fakeInjector = new FakeInjector(
            this.faker,
            objetMapper
        );
    }

    public List<Entity> createTestEntities(JpaRepository<Entity, Id> repository, int count) {
        var entities = new ArrayList<Entity>();
        for (int i = 0; i < count; i++) {
            var entity = createTestEntity(i);
            entities.add(entity);
        }

        return repository.saveAll(entities);
    }

    public void createTestUser(boolean passwordUser) {
        if (passwordUser) {
            var userInfo = PasswordUserInfo.builder()
                .password(faker.internet().password())
                .email(faker.internet().safeEmailAddress())
                .name(faker.name().name())
                .build();
            testUserEntity = userEntityRepository.save(
                UserEntity.builder()
                    .email(userInfo.email())
                    .name(userInfo.name())
                    .password(passwordEncoder.encode(userInfo.password()))
                    .build()
            );
        } else {
            testUserEntity = userEntityRepository.save(
                UserEntity.builder()
                    .email(faker.internet().emailAddress())
                    .password(passwordEncoder.encode(faker.internet().password()))
                    .snsId(faker.internet().uuid())
                    .provider(SocialProvider.GOOGLE)
                    .role(UserRole.USER)
                    .build()
            );
        }
    }

    public UserEntity getUserEntity(boolean passwordUser) {
        if (testUserEntity != null) {
            var isPasswordUser = testUserEntity.getProvider() == null;
            if (isPasswordUser == passwordUser) {
                return testUserEntity;
            }
        }

        createTestUser(passwordUser);
        return testUserEntity;
    }

    public UserEntity getUserEntity() {
        return testUserEntity;
    }
}
