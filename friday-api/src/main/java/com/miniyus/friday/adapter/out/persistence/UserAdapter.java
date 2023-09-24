package com.miniyus.friday.adapter.out.persistence;

import java.util.List;
import java.util.Optional;
import com.miniyus.friday.adapter.out.persistence.mapper.UserMapper;
import com.miniyus.friday.domain.users.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.miniyus.friday.common.hexagon.annotation.PersistenceAdapter;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserRepository;
import com.miniyus.friday.application.port.out.CreateUserPort;
import com.miniyus.friday.application.port.out.DeleteUserPort;
import com.miniyus.friday.application.port.out.RetrieveUserPort;
import com.miniyus.friday.application.port.out.UpdateUserPort;
import com.miniyus.friday.domain.users.User;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@RequiredArgsConstructor
@PersistenceAdapter
public class UserAdapter
        implements CreateUserPort, RetrieveUserPort, UpdateUserPort, DeleteUserPort {
    private final UserRepository userRepository;
    private final UserMapper mapper;

    @Override
    public User createUser(User domain) {
        var entity = userRepository.save(mapper.toEntity(domain));
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
    public Page<User> findAll(UserFilter searchUser, Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(
                searchUser,
                pageable);
        return userEntities.map(mapper::toDomain);
    }

    @Override
    public Optional<User> findById(Long id) {
        return userRepository.findById(id)
            .map(mapper::toDomain);
    }

    private User save(User user) {
        return mapper.toDomain(userRepository.save(mapper.toEntity(user)));
    }

    @Override
    public User updateUser(User user) {
        return this.save(user);
    }

    @Override
    public User resetPassword(User user) {
        return this.save(user);
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
