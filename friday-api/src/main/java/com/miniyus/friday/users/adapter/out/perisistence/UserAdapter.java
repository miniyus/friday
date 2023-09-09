package com.miniyus.friday.users.adapter.out.perisistence;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.users.application.port.out.CreateUserPort;
import com.miniyus.friday.users.application.port.out.DeleteUserPort;
import com.miniyus.friday.users.application.port.out.RetrieveUserPort;
import com.miniyus.friday.users.application.port.out.UpdateUserPort;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.User.SearchUser;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@RequiredArgsConstructor
@Component
public class UserAdapter
        implements CreateUserPort, RetrieveUserPort, UpdateUserPort, DeleteUserPort {
    private final UserRepository userRepository;

    @Override
    public User createUser(User domain) {
        var entity = userRepository.save(UserMapper.toEntity(domain));
        return UserMapper.toDomain(entity);
    }

    @Override
    public Collection<User> findAll() {
        Collection<UserEntity> userEntities = userRepository.findAll();
        return userEntities.stream().map(UserMapper::toDomain).toList();
    }

    @Override
    public Page<User> findAll(Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(pageable);
        return userEntities.map(UserMapper::toDomain);
    }

    @Override
    public Page<User> findAll(SearchUser searchUser, Pageable pageable) {
        Page<UserEntity> userEntities = userRepository.findAll(
                searchUser.getEmail(),
                searchUser.getName(),
                searchUser.getCreatedAtStart(),
                searchUser.getCreatedAtEnd(),
                searchUser.getUpdatedAtStart(),
                searchUser.getUpdatedAtEnd(),
                pageable);
        return userEntities.map(UserMapper::toDomain);
    }

    @Override
    public User findById(Long id) {
        return UserMapper.toDomain(userRepository.findById(id).orElse(null));
    }

    private User save(User user) {
        return UserMapper.toDomain(userRepository.save(UserMapper.toEntity(user)));
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

}
