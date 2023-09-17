package com.miniyus.friday.api.users.application.service;

import java.util.Collection;

import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.application.port.in.usecase.*;
import com.miniyus.friday.api.users.domain.User;
import com.miniyus.friday.common.pagination.SimplePage;
import com.miniyus.friday.api.users.application.exception.UserExistsException;
import com.miniyus.friday.api.users.application.exception.UserNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.miniyus.friday.common.hexagon.annotation.Usecase;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserRequest;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserQuery;
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

    /**
     * Creates a new user based on the provided command.
     *
     * @param request the request containing the user details
     * @return the created UserResource object
     */
    @Override
    public UserResource createUser(CreateUserRequest request) {
        User user = User.builder()
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .name(request.name())
            .role(request.role())
            .build();

        if (createUserPort.isUniqueEmail(user.getEmail())) {
            throw new UserExistsException();
        }

        return UserResource.fromDomain(createUserPort.createUser(user));
    }

    /**
     * Updates a user with the given command.
     *
     * @param id      user id
     * @param request the request containing the user ID, new name, and new role
     * @return the updated user
     */
    @Override
    public UserResource patchUser(Long id, UpdateUserRequest request) {
        User domain = updateUserPort.findById(id)
            .orElseThrow(UserNotFoundException::new);

        domain.patch(request.name(), request.role());

        return UserResource.fromDomain(updateUserPort.updateUser(domain));
    }

    /**
     * Retrieves all users.
     *
     * @return a collection of User objects representing all the users
     */
    @Override
    public Collection<UserResource> findAll() {
        return readUserPort.findAll()
            .stream()
            .map(UserResource::fromDomain)
            .toList();
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
    public Page<UserResource> findAll(RetrieveUserRequest request) {
        // find by conditions
        User.SearchUser search = User.SearchUser.builder()
            .email(request.getEmail())
            .name(request.getName())
            .createdAtStart(request.getCreatedAtStart())
            .createdAtEnd(request.getCreatedAtEnd())
            .updatedAtStart(request.getUpdatedAtStart())
            .updatedAtEnd(request.getUpdatedAtEnd())
            .build();

        Page<User> result;

        // only paginate
        if (search.isEmpty()) {
            result = readUserPort.findAll(request.getPageable());
        } else {
            result = readUserPort.findAll(search, request.getPageable());
        }

        var res = new SimplePage<>(
            result.getContent(),
            result.getTotalElements(),
            result.getPageable(),
            "users");

        return res.map(UserResource::fromDomain);
    }

    /**
     * Finds a user by their ID.
     *
     * @param id the ID of the user to find
     * @return the user with the specified ID
     */
    @Override
    public UserResource findById(Long id) {
        return UserResource.fromDomain(
            readUserPort.findById(id)
                .orElseThrow(UserNotFoundException::new)
        );
    }

    /**
     * Resets the password for a user.
     *
     * @param id       the ID of the user
     * @param password the new password
     * @return the updated user after resetting the password
     */
    @Override
    public UserResource resetPassword(Long id, String password) {
        User user = readUserPort.findById(id)
            .orElseThrow(UserNotFoundException::new);

        user.resetPassword(passwordEncoder.encode(password));

        return UserResource.fromDomain(
            updateUserPort.resetPassword(user)
        );
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
