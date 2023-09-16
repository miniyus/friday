package com.miniyus.friday.auth;

import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
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
 * Signup Controller
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    /**
     * Creates a new user account by signing up.
     *
     * @param authentication the user authentication information
     * @return the principal user info of the newly created user
     */
    @PostMapping(SecurityConfiguration.SIGNUP_URL)
    public ResponseEntity<PrincipalUserInfo> signup(
        @Valid @RequestBody PasswordUserInfo authentication) {
        var user = authService.signup(authentication);

        ServletUriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequestUri();
        builder.path("/v1/auth/me");
        var uri = builder.build().toUri();

        return ResponseEntity.created(uri).body(user);
    }

    @PostMapping(SecurityConfiguration.REFRESH_URL)
    public ResponseEntity<IssueToken> refresh(@RequestHeader(name = "RefreshToken") String refreshToken) {
        var tokens = authService.refresh(refreshToken);
        return ResponseEntity
            .created(null)
            .body(tokens);
    }

    @GetMapping(SecurityConfiguration.USERINFO_URL)
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<PrincipalUserInfo> userInfo() {
        var userInfo = authService.userInfo();
        return ResponseEntity.ok(userInfo);
    }

    @PostMapping(SecurityConfiguration.LOGOUT_URL)
    @PreAuthorize("hasAuthority('USER') or hasAuthority('ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void revokeToken() {
        authService.revokeToken();
    }
}
