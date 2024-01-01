package com.miniyus.friday.api.auth;

import com.miniyus.friday.api.auth.resource.AuthUserResource;
import com.miniyus.friday.infrastructure.config.SecurityConfiguration;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import com.miniyus.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import com.miniyus.friday.auth.domain.Token;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(name = "Auth")
public interface AuthApi {
    @Operation(summary = "oauth2 login",
        description = "login with oauth2",
        responses = {
            @ApiResponse(responseCode = "200", description = "ok",
                headers = {
                    @Header(name = "Authorization", description = "access token"),
                    @Header(name = "RefreshToken", description = "refresh token")}),
        }
    )
    @GetMapping(SecurityConfiguration.OAUTH2_LOGIN_URL + "/{provider}")
    void oauth2Login(@PathVariable String provider);

    @Operation(summary = "signin password user")
    @PostMapping(SecurityConfiguration.LOGIN_URL)
    ResponseEntity<PasswordTokenResponse> signin(
        @RequestBody PasswordAuthentication authentication);

    @Operation(summary = "refresh token",
        description = "refresh token")
    @PostMapping(SecurityConfiguration.REFRESH_URL)
    ResponseEntity<Token> refresh(
        @RequestHeader(name = "RefreshToken") String refreshToken);

    @Operation(summary = "user info")
    @GetMapping(SecurityConfiguration.USERINFO_URL)
    ResponseEntity<AuthUserResource> userInfo();

    @Operation(summary = "revoke token")
    @PostMapping(SecurityConfiguration.LOGOUT_URL)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void revokeToken();
}
