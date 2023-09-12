package com.miniyus.friday.infrastructure.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/09
 */
public interface CustomUserDetailsService extends UserDetailsService {
    public PrincipalUserInfo create(PasswordUserInfo authentication);
}
