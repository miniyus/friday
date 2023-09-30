package com.miniyus.friday.application;

import java.util.List;

import com.miniyus.friday.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.application.port.in.usecase.UpdateUserUsecase;
import com.miniyus.friday.domain.users.ResetPassword;
import com.miniyus.friday.domain.users.UpdateUser;
import com.miniyus.friday.domain.users.User;
import com.miniyus.friday.common.pagination.SimplePage;
import com.miniyus.friday.application.exception.UserExistsException;
import com.miniyus.friday.application.exception.UserNotFoundException;
import com.miniyus.friday.domain.users.UserFilter;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.application.port.out.CreateUserPort;
import com.miniyus.friday.application.port.out.DeleteUserPort;
import com.miniyus.friday.application.port.out.RetrieveUserPort;
import com.miniyus.friday.application.port.out.UpdateUserPort;
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
    public User createUser(User user) {
        if (createUserPort.isUniqueEmail(user.getEmail())) {
            throw new UserExistsException();
        }

        return createUserPort.createUser(user);
    }

    /**
     *
     * @param request update user
     * @return updated user
     */
    @Override
    public User patchUser(UpdateUser request) {
        User domain = updateUserPort.findById(request.id())
            .orElseThrow(UserNotFoundException::new);

        domain.patch(request.name(), request.role());

        return updateUserPort.updateUser(domain);
    }

    /**
     * Retrieves all users.
     *
     * @return a collection of User objects representing all the users
     */
    @Override
    public List<User> findAll() {
        return readUserPort.findAll();
    }

    /**
     * Retrieves a page of users based on the specified conditions in the given
     * RetrieveUserCommand.
     *
     * @param request the RetrieveUserCommand object containing the search criteria such as email,
     *                name, created at dates, and updated at dates
     * @return a Page object containing the list of users that match the search criteria
     */
    @Override
    public Page<User> findAll(UserFilter request, Pageable pageable) {
        Page<User> result;

        // only paginate
        if (request.isEmpty()) {
            result = readUserPort.findAll(pageable);
        } else {
            result = readUserPort.findAll(request, pageable);
        }

        return new SimplePage<>(
            result.getContent(),
            result.getTotalElements(),
            result.getPageable(),
            "users"
        );
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     */
    @Override
    public User findById(Long id) {
        return readUserPort.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean resetPassword(ResetPassword restPassword) {
        User user = readUserPort.findById(restPassword.id())
            .orElseThrow(UserNotFoundException::new);

        user.resetPassword(passwordEncoder.encode(restPassword.password()));

        return updateUserPort.resetPassword(user) != null;
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
