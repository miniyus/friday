package com.miniyus.friday.infrastructure.auth.login;

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
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/04
 */
@RestController
@RequiredArgsConstructor
public class SignupController {
    private final UserDetailsService userDetailsService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/signup")
    public ResponseEntity<PrincipalUserInfo> signup(@Valid @RequestBody PasswordUserInfo authentication) {
        PrincipalUserDetailsService service = (PrincipalUserDetailsService) userDetailsService;
        authentication.encodePassword(passwordEncoder);
        var user = service.create(authentication);

        return ResponseEntity.created(null).body(user);
    }

    @GetMapping("/jwt-test")
    @PreAuthorize("hasAuthority('USER')")
    public String jwtTest() {
        return "jwt-test";
    }
}
