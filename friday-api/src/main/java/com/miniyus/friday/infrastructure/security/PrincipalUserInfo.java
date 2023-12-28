package com.miniyus.friday.infrastructure.security;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.miniyus.friday.infrastructure.security.social.SocialProvider;
import com.miniyus.friday.users.domain.UserRole;
import jakarta.annotation.Nullable;
import lombok.Builder;
import lombok.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

/**
 * PrincipalUserInfo
 *
 * @author miniyus
 * @date 2023/08/31
 */
@Builder
@Value
@JsonIgnoreProperties({
    "password",
    "enabled",
    "accountNonExpired",
    "credentialsNonExpired",
    "accountNonLocked",
    "username"
})
public class PrincipalUserInfo implements UserDetails, OAuth2User {
    Long id;

    String snsId;

    SocialProvider provider;

    String email;

    String name;

    @Nullable
    String password;

    UserRole role;

    boolean enabled;

    boolean accountNonExpired;

    boolean credentialsNonExpired;

    boolean accountNonLocked;

    Map<String, Object> attributes;

    Collection<? extends GrantedAuthority> authorities;

    /**
     * Retrieves the attributes of the object.
     *
     * @return a map containing the attributes as key-value pairs
     */
    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Retrieves the collection of granted authorities for this user.
     *
     * @return the collection of granted authorities
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    /**
     * Returns the name of the object.
     *
     * @return the name of the object
     */
    @Override
    public String getName() {
        return name;
    }

    /**
     * Retrieves the password for this object.
     *
     * @return the password value stored in this object, or null if no password has been set
     */
    @Override
    @Nullable
    public String getPassword() {
        return password;
    }

    /**
     * Retrieves the username associated with this object.
     *
     * @return the email address used for the username
     */
    @Override
    public String getUsername() {
        return email;
    }

    /**
     * Returns a boolean value indicating whether the account is non-expired.
     *
     * @return a boolean value indicating whether the account is non-expired
     */
    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    /**
     * Retrieves the locked status of the account.
     *
     * @return true if the account is not locked, false otherwise
     */
    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    /**
     * Returns whether the credentials of the user are non-expired.
     *
     * @return true if the credentials are non-expired, false otherwise
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    /**
     * A description of the entire Java function.
     *
     * @return a boolean value indicating whether the function is enabled or not
     */
    @Override
    public boolean isEnabled() {
        return enabled;
    }

    /**
     * Retrieves the ID of the object.
     *
     * @return the ID of the object, or null if it does not have an ID.
     */
    @Nullable
    public Long getId() {
        return id;
    }

    /**
     * Checks if the user is an SNS user.
     *
     * @return true if the user is an SNS user, false otherwise
     */
    public boolean isSnsUser() {
        return (snsId != null && snsId.trim().isEmpty())
            && provider != null;
    }
}
