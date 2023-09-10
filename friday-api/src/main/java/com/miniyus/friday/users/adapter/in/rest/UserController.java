package com.miniyus.friday.users.adapter.in.rest;

import java.net.URI;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;
import com.miniyus.friday.common.hexagon.annotation.WebAdapter;
import com.miniyus.friday.common.response.SimplePage;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.users.adapter.in.rest.request.RetrieveUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.UpdateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.response.CreateUserResponse;
import com.miniyus.friday.users.adapter.in.rest.response.RetrieveUserResponse;
import com.miniyus.friday.users.adapter.in.rest.response.UpdateUserResponse;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserCommand;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.users.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.users.application.port.in.usecase.UpdateUserUsecase;
import com.miniyus.friday.users.domain.User;
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
@WebAdapter
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
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
     * @param pageable the pageable object specifying the page number, size, and sort order
     * @return a ResponseEntity containing a SimplePage of RetrieveUserResponse objects
     */
    @GetMapping("")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<SimplePage<RetrieveUserResponse>> retrieveUsers(
            @RequestParam(required = false) @Valid RetrieveUserRequest request,
            @PageableDefault(
                    page = 1,
                    size = 10,
                    sort = "createdAt",
                    direction = Direction.DESC) Pageable pageable) {

        RetrieveUserCommand cmd;
        if (request == null) {
            cmd = RetrieveUserCommand.builder()
                    .pageable(pageable)
                    .build();
        } else {
            cmd = request.toCommand(pageable);
        }

        Page<User> users = readUserQuery.findAll(cmd);

        log.debug("users count: " + users.getContent().size());
        SimplePage<RetrieveUserResponse> response = SimplePage.<User, RetrieveUserResponse>builder()
                .content(users.getContent())
                .map(RetrieveUserResponse::fromDomain)
                .pageable(users.getPageable())
                .totalElements(users.getTotalElements())
                .build();

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
    public ResponseEntity<UpdateUserResponse> updateUser(
            @PathVariable Long id,
            @Valid @RequestBody UpdateUserRequest request) {

        User update = updateUserUsecase.updateUser(request.toCommand(id));

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

        User update = updateUserUsecase.resetPassword(id, password.getPassword());

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
