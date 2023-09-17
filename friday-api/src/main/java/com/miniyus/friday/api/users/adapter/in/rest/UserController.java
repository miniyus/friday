package com.miniyus.friday.api.users.adapter.in.rest;

import java.net.URI;

import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.application.port.in.usecase.*;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.common.request.annotation.QueryParam;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.util.UriComponentsBuilder;
import com.miniyus.friday.api.users.application.port.in.usecase.ResetPasswordRequest;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserRequest;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.api.users.domain.User;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@RestAdapter(path = "v1/users")
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final CreateUserUsecase createUserUsecase;
    private final RetrieveUserQuery readUserQuery;
    private final UpdateUserUsecase updateUserUsecase;
    private final DeleteUserUsecase deleteUserUsecase;

    /**
     * Creates a new user.
     *
     * @param request              the createUser request
     * @param uriComponentsBuilder the uriComponentsBuilder for creating the location URI
     * @return the ResponseEntity containing the createUser response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<UserResource> createUser(
        @Valid @RequestBody CreateUserRequest request,
        UriComponentsBuilder uriComponentsBuilder) {

        UserResource create = createUserUsecase.createUser(request);

        URI uri = uriComponentsBuilder
            .path("/v1/users/{id}")
            .buildAndExpand(create.id())
            .toUri();

        return ResponseEntity
            .created(uri)
            .body(create);
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
            readUserQuery.findById(id)
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
        @QueryParam @Valid RetrieveUserRequest request) {

        RetrieveUserRequest req;
        if (request == null) {
            log.debug("req is null");
            req = RetrieveUserRequest.builder()
                .pageable(PageRequest.of(0, 20, Direction.DESC, "createdAt"))
                .build();
        } else {
            req = request;
        }

        Page<UserResource> users = readUserQuery.findAll(req);

        log.debug("users count: " + users.getContent().size());

        return ResponseEntity.ok(users);
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

        return ResponseEntity.ok(
            updateUserUsecase.patchUser(id, request)
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
    public ResponseEntity<UserResource> resetPassword(
        @PathVariable Long id,
        @RequestBody @Valid ResetPasswordRequest password) {

        return ResponseEntity.ok(
            updateUserUsecase.resetPassword(id, password.password())
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
        deleteUserUsecase.deleteById(id);
    }
}
