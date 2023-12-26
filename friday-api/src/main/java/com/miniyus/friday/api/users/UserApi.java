package com.miniyus.friday.api.users;

import com.miniyus.friday.common.request.annotation.QueryParam;
import com.miniyus.friday.infrastructure.config.RestConfiguration;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.users.adapter.in.rest.request.RetrieveUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.UpdateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.resource.ResetPasswordResource;
import com.miniyus.friday.users.adapter.in.rest.resource.UserResources;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "User API")
public interface UserApi {
    String PATH = RestConfiguration.PREFIX + "/users";

    @Operation(summary = "create user (admin)")
    @PostMapping(PATH)
    ResponseEntity<UserResources.UserResource> createUser(
        @Valid @RequestBody CreateUserRequest request);

    @GetMapping(PATH + "/{id}")
    ResponseEntity<UserResources.UserResource> retrieveUser(
        @PathVariable Long id);

    @GetMapping(PATH)
    ResponseEntity<Page<UserResources.UserResource>> retrieveUsers(
        @QueryParam @Valid RetrieveUserRequest request);

    @PatchMapping(PATH + "/{id}")
    ResponseEntity<UserResources.UserResource> patchUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request);

    @PatchMapping(PATH + "/{id}/reset-password")
    ResponseEntity<ResetPasswordResource> resetPassword(
        @PathVariable Long id,
        @RequestBody @Valid ResetPasswordRequest password);

    @DeleteMapping(PATH+"/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id);
}
