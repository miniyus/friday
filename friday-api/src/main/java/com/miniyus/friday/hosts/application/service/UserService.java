package com.miniyus.friday.hosts.application.service;

import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.common.pagination.SimplePage;
import com.miniyus.friday.users.application.exception.UserExistsException;
import com.miniyus.friday.users.application.exception.UserNotFoundException;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.users.application.port.in.usecase.UserUsecase;
import com.miniyus.friday.users.application.port.out.UserPort;
import com.miniyus.friday.users.domain.ResetPassword;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

/**
 * User service
 *
 * @author miniyus
 * @date 2023/09/09
 */
@RequiredArgsConstructor
@Usecase
public class UserService implements UserUsecase, RetrieveUserQuery {
    private final UserPort userPort;

    private final PasswordEncoder passwordEncoder;

    @Override
    public User createUser(User user) {
        if (userPort.isUniqueEmail(user.getEmail())) {
            throw new UserExistsException();
        }

        return userPort.createUser(user);
    }

    /**
     *
     * @param request update user
     * @return updated user
     */
    @Override
    public User patchUser(User request) {
        User domain = userPort.findById(request.getId())
            .orElseThrow(UserNotFoundException::new);

        domain.patch(request.getName(), request.getRole());

        return userPort.updateUser(domain);
    }

    /**
     * Retrieves all users.
     *
     * @return a collection of User objects representing all the users
     */
    @Override
    public List<User> findAll() {
        return userPort.findAll();
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
            result = userPort.findAll(pageable);
        } else {
            result = userPort.findAll(request, pageable);
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
        return userPort.findById(id)
            .orElseThrow(UserNotFoundException::new);
    }

    @Override
    public boolean resetPassword(ResetPassword restPassword) {
        User user = userPort.findById(restPassword.id())
            .orElseThrow(UserNotFoundException::new);

        user.resetPassword(passwordEncoder.encode(restPassword.password()));

        return userPort.resetPassword(user) != null;
    }

    /**
     * Deletes an entity by ID.
     *
     * @param id the ID of the entity to delete
     */
    @Override
    public void deleteUserById(Long id) {
        userPort.deleteById(id);
    }
}
