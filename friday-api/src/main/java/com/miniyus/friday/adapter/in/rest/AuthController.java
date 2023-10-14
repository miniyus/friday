package com.miniyus.friday.adapter.in.rest;

import com.miniyus.friday.adapter.in.rest.resource.UserResources.AuthUserResource;
import com.miniyus.friday.application.port.in.query.RetrieveUserInfoQuery;
import com.miniyus.friday.application.port.in.usecase.AuthUsecase;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.domain.auth.Auth;
import com.miniyus.friday.domain.auth.Token;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.config.SecurityConfiguration;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

/**
 * Auth Controller
 *
 * @author miniyus
 * @date 2023/09/04
 */
@RestAdapter
@RequiredArgsConstructor
public class AuthController {
    private final RetrieveUserInfoQuery retrieveUserInfoQuery;
    private final AuthUsecase  authUsecase;

    /**
     * Creates a new user account by signing up.
     *
     * @param authentication the user authentication information
     * @return the principal user info of the newly created user
     */
    @PostMapping(SecurityConfiguration.SIGNUP_URL)
    public ResponseEntity<AuthUserResource> signup(
        @Valid @RequestBody PasswordUserInfo authentication) {
        // Call the authService.signup() method to sign up the user and get the user information

        var authDomain = Auth.builder()
            .email(authentication.email())
            .name(authentication.name())
            .password(authentication.password())
            .build();

        var user = authUsecase.signup(authDomain);

        // Build the URI for the /v1/auth/me endpoint
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        builder.path("/v1/auth/me");
        var uri = builder.build().toUri();

        // Return a ResponseEntity with the created URI and the user information
        return ResponseEntity.created(uri).body(AuthUserResource.fromDomain(user));
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The response entity containing the issued access token.
     */
    @PostMapping(SecurityConfiguration.REFRESH_URL)
    public ResponseEntity<Token> refresh(
        @RequestHeader(name = "RefreshToken") String refreshToken) {
        // Call the authService to refresh the token
        var tokens = authUsecase.refreshToken(refreshToken);
        // Create a response entity with the issued access token
        return ResponseEntity.ok(tokens);
    }

    @GetMapping(SecurityConfiguration.USERINFO_URL)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<AuthUserResource> userInfo() {
        var userInfo = retrieveUserInfoQuery.retrieveUserInfo();
        return ResponseEntity.ok(AuthUserResource.fromDomain(userInfo));
    }

    @PostMapping(SecurityConfiguration.LOGOUT_URL)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken() {
        authUsecase.revokeToken();
    }
}
