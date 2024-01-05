package com.meteormin.friday.api.users;

import com.meteormin.friday.api.users.request.CreateUserRequest;
import com.meteormin.friday.api.users.request.ResetPasswordRequest;
import com.meteormin.friday.api.users.request.RetrieveUserRequest;
import com.meteormin.friday.api.users.request.UpdateUserRequest;
import com.meteormin.friday.api.users.resource.ResetPasswordResource;
import com.meteormin.friday.api.users.resource.UserResources;
import com.meteormin.friday.common.request.annotation.QueryParam;
import com.meteormin.friday.infrastructure.config.RestConfiguration;
import com.meteormin.friday.infrastructure.security.PrincipalUserInfo;
import com.meteormin.friday.infrastructure.security.annotation.AuthUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Users")
public interface UserApi {
    String PATH = RestConfiguration.PREFIX + "/users";

    @Operation(summary = "create user (admin)")
    @PostMapping(PATH)
    ResponseEntity<UserResources.UserResource> createUser(
        @Valid @RequestBody CreateUserRequest request);

    @GetMapping(PATH + "/{id}")
    ResponseEntity<UserResources.UserResource> retrieveUser(
        @PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo);

    @GetMapping(PATH)
    ResponseEntity<Page<UserResources.UserResource>> retrieveUsers(
        @QueryParam @Valid RetrieveUserRequest request,
        @PageableDefault(page = 1,
            sort = "createdAt",
            direction = Direction.DESC) Pageable pageable);

    @PatchMapping(PATH + "/{id}")
    ResponseEntity<UserResources.UserResource> patchUser(
        @PathVariable Long id,
        @Valid @RequestBody UpdateUserRequest request,
        @AuthUser PrincipalUserInfo userInfo);

    @PatchMapping(PATH + "/{id}/reset-password")
    ResponseEntity<ResetPasswordResource> resetPassword(
        @PathVariable Long id,
        @RequestBody @Valid ResetPasswordRequest password,
        @AuthUser PrincipalUserInfo userInfo);

    @DeleteMapping(PATH + "/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteUser(@PathVariable Long id,
        @AuthUser PrincipalUserInfo userInfo);
}
