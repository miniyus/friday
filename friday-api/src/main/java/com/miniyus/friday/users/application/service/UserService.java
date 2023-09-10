package com.miniyus.friday.users.application.service;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserCommand;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.users.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserUsecase;
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
 * @date 2023/09/09
 */
@RequiredArgsConstructor
@Usecase
public class UserService
        implements CreateUserUsecase, RetrieveUserQuery, UpdateUserUsecase, DeleteUserUsecase {
    private final CreateUserPort createUserPort;

    private final RetrieveUserPort readUserPort;

    private final UpdateUserPort updateUserPort;

    private final DeleteUserPort deleteUserPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(CreateUserCommand command) {
        User user = new User(
                null,
                command.getEmail(),
                command.getPassword(),
                command.getName(),
                command.getRole(),
                null,
                null,
                null,
                null,
                null);

        return createUserPort.createUser(user);
    }

    @Override
    public User updateUser(UpdateUserCommand command) {
        User domain = updateUserPort.findById(command.getId());
        domain.updateName(command.getName());
        domain.updateRole(command.getRole());

        return updateUserPort.updateUser(domain);
    }

    @Override
    public Collection<User> findAll() {
        return readUserPort.findAll();
    }

    @Override
    public Page<User> findAll(RetrieveUserCommand command) {
        // find by conditions
        SearchUser search = SearchUser.builder()
                .email(command.getEmail())
                .name(command.getName())
                .createdAtStart(command.getCreatedAtStart())
                .createdAtEnd(command.getCreatedAtEnd())
                .updatedAtStart(command.getUpdatedAtStart())
                .updatedAtEnd(command.getUpdatedAtEnd())
                .build();

        // only paginate
        if (search.isEmpty()) {
            return readUserPort.findAll(command.getPageable());
        }

        return readUserPort.findAll(search, command.getPageable());
    }

    @Override
    public User findById(Long id) {
        return readUserPort.findById(id);
    }

    @Override
    public User resetPassword(Long id, String password) {
        User user = readUserPort.findById(id);

        user.resetPassword(passwordEncoder.encode(password));

        return updateUserPort.resetPassword(user);
    }

    @Override
    public void deleteById(Long id) {
        deleteUserPort.deleteById(id);
    }
}
