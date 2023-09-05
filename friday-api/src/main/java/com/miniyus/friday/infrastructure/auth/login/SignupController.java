package com.miniyus.friday.infrastructure.auth.login;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import com.miniyus.friday.infrastructure.auth.PrincipalUserDetailsService;
import com.miniyus.friday.infrastructure.auth.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.auth.login.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;

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
public class SignupController {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfiguration jwtConfiguration;

    /**
     * Creates a new user account by signing up.
     *
     * @param authentication the user authentication information
     * @return the principal user info of the newly created user
     */
    @PostMapping("/signup")
    public ResponseEntity<PrincipalUserInfo> signup(@Valid @RequestBody PasswordUserInfo authentication) {
        PrincipalUserDetailsService service = (PrincipalUserDetailsService) userDetailsService;
        authentication.encodePassword(passwordEncoder);
        var user = service.create(authentication);

        return ResponseEntity.created(null).body(user);
    }

    /**
     * Retrieves the JWT configuration details.
     *
     * @return A ResponseEntity containing a map with the access, and
     *         refresh values of the JWT configuration.
     */
    @GetMapping("/jwt-config")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Map<String, Object>> jwtTest() {
        Map<String, Object> resMap = new HashMap<String, Object>();
        resMap.put("access", jwtConfiguration.getAccess());
        resMap.put("refresh", jwtConfiguration.getRefresh());

        return ResponseEntity.ok().body(resMap);
    }
}
