package com.miniyus.friday.adapter.in.rest;

import com.miniyus.friday.adapter.in.rest.resource.UserResources.AuthUserResource;
import com.miniyus.friday.adapter.out.persistence.AuthAdapter;
import com.miniyus.friday.common.hexagon.annotation.RestAdapter;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.security.config.SecurityConfiguration;
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
    private final AuthAdapter authService;

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
        var user = authService.signup(authentication);

        // Build the URI for the /v1/auth/me endpoint
        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        builder.path("/v1/auth/me");
        var uri = builder.build().toUri();

        // Return a ResponseEntity with the created URI and the user information
        return ResponseEntity.created(uri).body(AuthUserResource.fromPrincipalUserInfo(user));
    }

    /**
     * Refreshes the access token using the provided refresh token.
     *
     * @param refreshToken The refresh token.
     * @return The response entity containing the issued access token.
     */
    @PostMapping(SecurityConfiguration.REFRESH_URL)
    public ResponseEntity<IssueToken> refresh(
        @RequestHeader(name = "RefreshToken") String refreshToken) {
        // Call the authService to refresh the token
        var tokens = authService.refresh(refreshToken);
        // Create a response entity with the issued access token
        return ResponseEntity.ok(tokens);
    }

    @GetMapping(SecurityConfiguration.USERINFO_URL)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<AuthUserResource> userInfo() {
        var userInfo = authService.userInfo();

        return ResponseEntity.ok(AuthUserResource.fromPrincipalUserInfo(userInfo));
    }

    @PostMapping(SecurityConfiguration.LOGOUT_URL)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken() {
        authService.revokeToken();
    }
}
