package com.miniyus.friday.infrastructure.security.auth;

import java.util.HashMap;
import java.util.Map;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * Signup Controller
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1")
public class AuthController {
    private final CustomUserDetailsService userDetailsService;
    private final JwtService jwtService;
    private final JwtConfiguration jwtConfiguration;
    private final PasswordEncoder passwordEncoder;

    /**
     * Creates a new user account by signing up.
     *
     * @param authentication the user authentication information
     * @return the principal user info of the newly created user
     */
    @PostMapping("/auth/signup")
    public ResponseEntity<PrincipalUserInfo> signup(
            @Valid @RequestBody PasswordUserInfo authentication) {
        authentication.encodePassword(passwordEncoder);
        try {
            var user = userDetailsService.create(authentication);
            return ResponseEntity.created(null).body(user);
        } catch(Throwable throwable) {
            throw new RestErrorException("error.userExists", RestErrorCode.CONFLICT);
        }
    }

    /**
     * Retrieves the JWT configuration details.
     *
     * @return A ResponseEntity containing a map with the access, and refresh values of the JWT
     *         configuration.
     */
    @GetMapping("/auth/jwt-config")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> jwtTest() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("access", jwtConfiguration.getAccess());
        resMap.put("refresh", jwtConfiguration.getRefresh());

        return ResponseEntity.ok().body(resMap);
    }

    @PostMapping("/auth/refresh")
    public ResponseEntity<IssueToken> refresh(@RequestBody String refreshToken) {
        UserEntity user = jwtService.getUserByRefreshToken(refreshToken).orElse(null);

        if(user == null) {
            throw new AccessDeniedException("error.accessDenied");
        }

        return ResponseEntity
                .created(null)
                .body(jwtService.issueToken(user.getId()));
    }
}
