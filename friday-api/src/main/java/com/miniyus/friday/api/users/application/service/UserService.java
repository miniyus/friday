package com.miniyus.friday.api.users.application.service;

import java.util.Collection;

import com.miniyus.friday.api.users.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.api.users.application.port.in.usecase.UpdateUserCommand;
import com.miniyus.friday.api.users.application.port.in.usecase.UpdateUserUsecase;
import com.miniyus.friday.api.users.domain.User;
import com.miniyus.friday.common.pagination.SimplePage;
import com.miniyus.friday.api.users.application.exception.UserExistsException;
import com.miniyus.friday.api.users.application.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserCommand;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.api.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.api.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.api.users.application.port.out.CreateUserPort;
import com.miniyus.friday.api.users.application.port.out.DeleteUserPort;
import com.miniyus.friday.api.users.application.port.out.RetrieveUserPort;
import com.miniyus.friday.api.users.application.port.out.UpdateUserPort;
import lombok.RequiredArgsConstructor;

/**
 * User service
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
        User user = User.builder()
                .email(command.email())
                .password(passwordEncoder.encode(command.password()))
                .name(command.name())
                .role(command.role())
                .build();
        if (createUserPort.isUniqueEmail(user.getEmail())) {
            throw new UserExistsException();
        }

        return createUserPort.createUser(user);
    }

    /**
     * Updates a user with the given command.
     *
     * @param command the command containing the user ID, new name, and new role
     * @return the updated user
     */
    @Override
    public User patchUser(UpdateUserCommand command) {
        User domain = updateUserPort.findById(command.id());
        domain.patch(command.name(), command.role());

        return updateUserPort.updateUser(domain);
    }

    /**
     * Retrieves all users.
     *
     * @return a collection of User objects representing all the users
     */
    @Override
    public Collection<User> findAll() {
        return readUserPort.findAll();
    }

    /**
     * Retrieves a page of users based on the specified conditions in the given RetrieveUserCommand.
     *
     * @param command the RetrieveUserCommand object containing the search criteria such as email,
     *        name, created at dates, and updated at dates
     * @return a Page object containing the list of users that match the search criteria
     */
    @Override
    public Page<User> findAll(RetrieveUserCommand command) {
        // find by conditions
        User.SearchUser search = User.SearchUser.builder()
                .email(command.email())
                .name(command.name())
                .createdAtStart(command.createdAtStart())
                .createdAtEnd(command.createdAtEnd())
                .updatedAtStart(command.updatedAtStart())
                .updatedAtEnd(command.updatedAtEnd())
                .build();

        Page<User> result;

        // only paginate
        if (search.isEmpty()) {
            result = readUserPort.findAll(command.pageable());
        } else {
            result = readUserPort.findAll(search, command.pageable());
        }

        return new SimplePage<>(
                result.getContent(),
                result.getTotalElements(),
                result.getPageable(),
                "users");
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     */
    @Override
    public User findById(Long id) {
        var user = readUserPort.findById(id);

        if (user == null) {
            throw new UserNotFoundException();
        }

        return user;
    }

    /**
     * Resets the password for a user.
     *
     * @param id the ID of the user
     * @param password the new password
     * @return the updated user after resetting the password
     */
    @Override
    public User resetPassword(Long id, String password) {
        User user = readUserPort.findById(id);

        user.resetPassword(passwordEncoder.encode(password));

        return updateUserPort.resetPassword(user);
    }

    /**
     * Deletes an entity by ID.
     *
     * @param id the ID of the entity to delete
     */
    @Override
    public void deleteById(Long id) {
        deleteUserPort.deleteById(id);
    }
}
