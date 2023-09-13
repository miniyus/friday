package com.miniyus.friday.infrastructure.security;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.miniyus.friday.infrastructure.security.oauth2.OAuth2Provider;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@Builder
@Value
public class PrincipalUserInfo implements UserDetails, OAuth2User {
    Long id;

    String snsId;

    OAuth2Provider provider;

    String username;

    String name;

    @Nullable
    @JsonIgnore
    String password;

    String role;

    boolean enabled;

    boolean accountNonExpired;

    boolean credentialsNonExpired;

    boolean accountNonLocked;

    Map<String, Object> attributes;

    Collection<? extends GrantedAuthority> authorities;

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    @Nullable
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Nullable
    public Long getId() {
        return id;
    }

    public boolean isSnsUser() {
        return (snsId != null && snsId.trim().isEmpty())
            && provider != null;
    }
}
