package com.miniyus.friday.api.users.adapter.in.rest;

import java.net.URI;

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
import com.miniyus.friday.api.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.api.users.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.api.users.adapter.in.rest.request.RetrieveUserRequest;
import com.miniyus.friday.api.users.adapter.in.rest.request.PatchUserRequest;
import com.miniyus.friday.api.users.adapter.in.rest.response.CreateUserResponse;
import com.miniyus.friday.api.users.adapter.in.rest.response.RetrieveUserResponse;
import com.miniyus.friday.api.users.adapter.in.rest.response.UpdateUserResponse;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserCommand;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.api.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.api.users.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.api.users.application.port.in.usecase.UpdateUserUsecase;
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
     * @param request the createUser request
     * @param uriComponentsBuilder the uriComponentsBuilder for creating the location URI
     * @return the ResponseEntity containing the createUser response
     */
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request,
            UriComponentsBuilder uriComponentsBuilder) {

        User create = createUserUsecase.createUser(request.toCommand());

        CreateUserResponse response = CreateUserResponse.fromDomain(create);

        URI uri = uriComponentsBuilder.path("/v1/users/{id}")
                .buildAndExpand(create.getId())
                .toUri();

        return ResponseEntity.created(uri).body(response);
    }

    /**
     * Retrieves a user based on the provided ID.
     *
     * @param id the ID of the user to retrieve
     * @return the ResponseEntity containing the RetrieveUserResponse
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<RetrieveUserResponse> retrieveUser(
            @PathVariable Long id) {
        User read = readUserQuery.findById(id);

        RetrieveUserResponse response = RetrieveUserResponse.fromDomain(read);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Retrieves a page of users based on the provided request.
     *
     * @param request the request object containing the parameters for retrieving users
     * @return a ResponseEntity containing a SimplePage of RetrieveUserResponse objects
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Page<RetrieveUserResponse>> retrieveUsers(
            @QueryParam @Valid RetrieveUserRequest request) {

        RetrieveUserCommand cmd;
        if (request == null) {
            log.debug("req is null");
            cmd = RetrieveUserCommand.builder()
                    .pageable(PageRequest.of(0, 20, Direction.DESC, "createdAt"))
                    .build();
        } else {
            log.debug("page? {}", request.getPage());
            log.debug("size? {}", request.getSize());
            cmd = request.toCommand();
        }

        Page<User> users = readUserQuery.findAll(cmd);

        log.debug("users count: " + users.getContent().size());

        var response = users.map(RetrieveUserResponse::fromDomain);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Updates a user.
     *
     * @param request the update user request object
     * @return the response entity containing the updated user
     */
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<UpdateUserResponse> patchUser(
            @PathVariable Long id,
            @Valid @RequestBody PatchUserRequest request) {

        User update = updateUserUsecase.patchUser(request.toCommand(id));

        UpdateUserResponse response = UpdateUserResponse.fromDomain(update);

        return ResponseEntity.ok().body(response);
    }

    /**
     * Resets the password for a user.
     *
     * @param id the ID of the user
     * @param password the new password
     * @return the response entity containing the updated user response
     */
    @PatchMapping("/{id}/reset-password")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    public ResponseEntity<UpdateUserResponse> resetPassword(
            @PathVariable Long id,
            @RequestBody @Valid ResetPasswordRequest password) {

        User update = updateUserUsecase.resetPassword(id, password.password());

        return ResponseEntity.ok().body(UpdateUserResponse.fromDomain(update));
    }

    /**
     * Deletes a user by their ID.
     *
     * @param id the ID of the user to be deleted
     * @return void
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN') or principal.id == #id")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUser(@PathVariable Long id) {
        deleteUserUsecase.deleteById(id);
    }
}