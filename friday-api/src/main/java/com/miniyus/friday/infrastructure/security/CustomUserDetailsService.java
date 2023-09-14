package com.miniyus.friday.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface CustomUserDetailsService extends UserDetailsService {
    PrincipalUserInfo create(PasswordUserInfo authentication);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
