package com.miniyus.friday.users.adapter.in.rest;

import com.miniyus.friday.api.users.UserApi;
import com.miniyus.friday.common.hexagon.BaseController;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.users.adapter.in.rest.request.RetrieveUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.UpdateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.resource.ResetPasswordResource;
import com.miniyus.friday.users.adapter.in.rest.resource.UserResources;
import com.miniyus.friday.users.adapter.in.rest.resource.UserResources.UserResource;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.users.application.port.in.usecase.UserUsecase;
import com.miniyus.friday.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * User Controller
 *
 * @author miniyus
 * @since 2023/09/02
 */
@RestAdapter(path = UserApi.PATH)
@RequiredArgsConstructor
@Slf4j
public class UserController extends BaseController implements UserApi {
    private final UserUsecase userUsecase;
    private final RetrieveUserQuery readUserQuery;

    /**
     * Creates a new user.
     *
     * @param request the createUser request
     * @return the ResponseEntity containing the createUser response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResource> createUser(
        @Valid @RequestBody CreateUserRequest request) {
        User create = userUsecase.createUser(request.toDomain());

        return ResponseEntity
            .created(createUri("/{id}", create.getId()))
            .body(UserResource.fromDomain(create));
    }

    /**
     * Retrieves a user based on the provided ID.
     *
     * @param id the ID of the user to retrieve
     * @return the ResponseEntity containing the RetrieveUserResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<UserResource> retrieveUser(
        @PathVariable Long id) {
        return ResponseEntity.ok(
            UserResource.fromDomain(readUserQuery.findById(id))
        );
    }

    /**
     * Retrieves a page of users based on the provided request.
     *
     * @param request the request object containing the parameters for retrieving users
     * @return a ResponseEntity containing a SimplePage of RetrieveUserResponse objects
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<UserResource>> retrieveUsers(
        @QueryParam @Valid RetrieveUserRequest request,
        @PageableDefault(page = 1,
        sort = "createdAt",
        direction = Direction.DESC) Pageable pageable) {

        Page<User> users = readUserQuery.findAll(request.toDomain());

        log.debug("users count: " + users.getContent().size());

        return ResponseEntity.ok(UserResources.fromDomains(users));
    }

    /**
     * Updates a user.
     *
     * @param request the update user request object
     * @return the response entity containing the updated user
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<UserResource> patchUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request) {

        var updated = userUsecase.patchUser(request.toDomain(id));

        return ResponseEntity.ok(
            UserResource.fromDomain(updated)
        );
    }

    /**
     * Resets the password for a user.
     *
     * @param id       the ID of the user
     * @param password the new password
     * @return the response entity containing the updated user response
     */
    @PatchMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<ResetPasswordResource> resetPassword(
        @PathVariable Long id,
        @RequestBody @Valid ResetPasswordRequest password) {

        var updated = userUsecase.resetPassword(password.toDomain(id));
        return ResponseEntity.ok(
            new ResetPasswordResource(updated)
        );
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        userUsecase.deleteUserById(id);
    }
}
