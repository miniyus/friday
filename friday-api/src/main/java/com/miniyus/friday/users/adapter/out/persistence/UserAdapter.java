package com.miniyus.friday.users.adapter.out.persistence;

import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.users.adapter.out.persistence.mapper.UserMapper;
import com.miniyus.friday.users.application.port.out.UserPort;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

/**
 * [description]
 *
 * @author miniyus
 * @since 2023/09/02
 */
@RequiredArgsConstructor
@PersistenceAdapter
public class UserAdapter
    implements UserPort {
    private final UserEntityRepository userRepository;
    private final UserMapper mapper;

    @Override
    public User createUser(User domain) {
        var entity = userRepository.save(mapper.create(domain));
        return mapper.toDomain(entity);
    }

    @Override
    public List<User> findAll() {
        List<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(mapper::toDomain).toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(mapper::toDomain);
    }

    @Override
    public Page<User> findAll(UserFilter searchUser) {
        Page<UserEntity> userEntities = userRepository.findUsers(searchUser);
        return userEntities.map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
            .map(mapper::toDomain);
    }

    private User save(UserEntity entity) {
        return mapper.toDomain(userRepository.save(entity));
    }

    @Override
    public User updateUser(User user) {
        var entity = mapper.toEntity(user);
        return this.save(entity);
    }

    @Override
    public User resetPassword(User user) {
        return updateUser(user);
    }

    @Override
    public void deleteById(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public boolean isUniqueEmail(String email) {
        return userRepository.existsByEmail(email);
    }
}
