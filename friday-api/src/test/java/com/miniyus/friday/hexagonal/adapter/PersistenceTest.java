package com.miniyus.friday.hexagonal.adapter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.javafaker.Faker;
import com.precisionbio.cuttysark.AbstractTestContainerTest;
import com.precisionbio.cuttysark.common.fake.FakeInjector;
import com.precisionbio.cuttysark.infrastructure.persistence.BaseEntity;
import com.precisionbio.cuttysark.infrastructure.persistence.entities.UserEntity;
import com.precisionbio.cuttysark.infrastructure.persistence.repositories.UserEntityRepository;
import com.precisionbio.cuttysark.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.precisionbio.cuttysark.infrastructure.security.social.SocialProvider;
import com.precisionbio.cuttysark.users.domain.Client;
import com.precisionbio.cuttysark.users.domain.UserRole;
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

    public void createTestUser(Client client) {
        if (client.equals(Client.CONSOLE)) {
            testUserEntity = userEntityRepository.save(
                UserEntity.create(
                    PasswordUserInfo.builder()
                        .password(faker.internet().password())
                        .email(faker.internet().safeEmailAddress())
                        .name(faker.name().username())
                        .department(faker.lorem().word())
                        .build(),
                    passwordEncoder
                )
            );
        } else {
            testUserEntity = userEntityRepository.save(
                UserEntity.builder()
                    .client(client)
                    .username(faker.internet().emailAddress())
                    .password(passwordEncoder.encode(faker.internet().password()))
                    .snsId(faker.internet().uuid())
                    .role(UserRole.USER)
                    .registrationId(SocialProvider.GOOGLE.value())
                    .provider(SocialProvider.GOOGLE)
                    .build()
            );
        }
    }

    public UserEntity getUserEntity(Client client) {
        if (testUserEntity != null && testUserEntity.getClient().equals(client)) {
            return testUserEntity;
        }

        createTestUser(client);
        return testUserEntity;
    }

    public UserEntity getUserEntity() {
        return testUserEntity;
    }
}
