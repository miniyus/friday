package com.miniyus.friday.integration.adapter.in.rest;

import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import com.miniyus.friday.integration.document.UserDocument;
import com.miniyus.friday.users.adapter.in.rest.UserController;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.users.adapter.in.rest.request.UpdateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.resource.ResetPasswordResource;
import com.miniyus.friday.users.adapter.in.rest.resource.UserResources.UserResource;
import com.miniyus.friday.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.users.application.port.in.usecase.UserUsecase;
import com.miniyus.friday.users.domain.User;
import com.miniyus.friday.users.domain.UserRole;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

/**
 * user controller test
 *
 * @author miniyus
 * @date 2023/09/07
 */
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends UserDocument {
    @MockBean
    private UserUsecase userUsecase;

    @MockBean
    private RetrieveUserQuery retrieveUserQuery;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private User domain;

    private void buildUserDomain(Long id) {
        domain = new User(
            id,
            faker.internet().safeEmailAddress(),
            faker.internet().password(),
            faker.name().fullName(),
            UserRole.USER.value(),
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null
        );
    }

    private CreateUserRequest buildCreateUserRequest(Long id) {
        buildUserDomain(id);
        return CreateUserRequest
            .builder()
            .email(domain.getEmail())
            .name(domain.getName())
            .password(domain.getPassword())
            .role(domain.getRole())
            .build();
    }

    private UserResource buildCreateUserResponse() {
        when(userUsecase.createUser(any())).thenReturn(domain);
        return UserResource.fromDomain(domain);
    }

    @Test
    @WithMockCustomUser(role = UserRole.ADMIN)
    public void createUserTest() throws Exception {
        Long id = 1L;
        var request = buildCreateUserRequest(id);
        var response = buildCreateUserResponse();
        createUser(request, response);
    }

    private UserResource buildRetrieveUserResponse(Long id) {
        buildUserDomain(id);
        when(retrieveUserQuery.findById(any(Long.class))).thenReturn(domain);
        return UserResource.fromDomain(domain);
    }

    @Test
    @WithMockCustomUser
    void retrieveUserTest() throws Exception {
        Long id = 1L;
        var response = buildRetrieveUserResponse(id);
        retrieveUser(id, response);
    }

    private UpdateUserRequest buildUpdateUserRequest() {
        return UpdateUserRequest
            .builder()
            .name(faker.name().fullName())
            .role(UserRole.USER.value())
            .build();
    }

    private UserResource buildUpdateUserResponse(Long id, UpdateUserRequest request) {
        buildUserDomain(id);

        domain.patch(
            request.name(),
            request.role()
        );

        when(userUsecase.patchUser(any())).thenReturn(domain);

        return UserResource.fromDomain(domain);
    }

    @Test
    @WithMockCustomUser
    public void updateUserTest() throws Exception {
        Long id = 1L;
        var request = buildUpdateUserRequest();
        var response = buildUpdateUserResponse(id, request);
        updateUser(id, request, response);
    }

    private ResetPasswordRequest buildResetPasswordRequest() {
        return new ResetPasswordRequest(
            faker.internet().password()
        );
    }

    private ResetPasswordResource buildResetPasswordResponse(Long id,
        ResetPasswordRequest request) {
        buildUserDomain(id);

        domain.resetPassword(
            passwordEncoder.encode(request.password())
        );

        boolean isMatches = passwordEncoder.matches(request.password(), domain.getPassword());

        when(userUsecase.resetPassword(any())).thenReturn(
            isMatches
        );

        return new ResetPasswordResource(isMatches);
    }

    @Test
    @WithMockCustomUser
    public void restPasswordTest() throws Exception {
        Long id = 1L;
        var request = buildResetPasswordRequest();
        var response = buildResetPasswordResponse(id, request);
        resetPassword(id, request, response);
    }

    @Test
    @WithMockCustomUser
    public void deleteUserTest() throws Exception {
        Long id = 1L;
        deleteUser(id);
    }
}
