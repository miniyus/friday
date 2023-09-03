package com.miniyus.friday.users.adapter.in.rest;

import java.net.URI;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.response.CreateUserResponse;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.users.domain.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/02
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/users")
public class UserController {
    private final CreateUserUsecase createUserUsecase;

    @PostMapping("/")
    public ResponseEntity<CreateUserResponse> createUser(
            @Valid @RequestBody CreateUserRequest request,
            UriComponentsBuilder uriComponentsBuilder) {
        User create = createUserUsecase.createUser(request.toCommand());
        CreateUserResponse response = CreateUserResponse.fromDomain(create);
        URI uri = uriComponentsBuilder.path("/v1/users/{id}").buildAndExpand(create.getId()).toUri();
        return ResponseEntity.created(uri).body(response);
    }
}
