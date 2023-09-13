package com.miniyus.friday.users.application.service;

import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.support.PageableUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
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

        if (createUserPort.isUniqueEmail(user.getEmail())) {
            throw new RestErrorException(RestErrorCode.CONFLICT, "error.userExists");
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
        User domain = updateUserPort.findById(command.getId());
        domain.patch(command.getName(), command.getRole());

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
            throw new RestErrorException(
                    RestErrorCode.NOT_FOUND,
                    "error.userNotFound");
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
