package com.meteormin.friday.infrastructure.security;

import com.meteormin.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * [description]
 *
 * @author meteormin
 * @since 2023/09/09
 */
public interface CustomUserDetailsService extends UserDetailsService {
    PrincipalUserInfo create(PasswordUserInfo authentication);

    void setPasswordEncoder(PasswordEncoder passwordEncoder);
}
