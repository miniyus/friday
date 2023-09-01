package com.miniyus.friday.infrastructure.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;
import com.miniyus.friday.infrastructure.jpa.entities.UserJpaEntity;
import com.miniyus.friday.infrastructure.oauth2.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/08/31
 */
@RequiredArgsConstructor
public class PrincipalUserInfo implements UserDetails, OAuth2User {
    private final UserJpaEntity userEntity;
    private final OAuth2UserInfo userInfo;

    @Override
    public Map<String, Object> getAttributes() {
        return userInfo.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        // TODO: authorities 추가
        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return null;
            }
        });

        return authorities;
    }

    @Override
    public String getName() {
        return userInfo.getName();
    }

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public String getUsername() {
        return userInfo.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return userEntity.getDeletedAt() == null;
    }

    @Override
    public boolean isAccountNonLocked() {
        return userEntity.getDeletedAt() == null;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return userEntity.getDeletedAt() == null;
    }

    @Override
    public boolean isEnabled() {
        return userEntity.getDeletedAt() == null;
    }

    public UserJpaEntity getUserEntity() {
        return userEntity;
    }

    public OAuth2UserInfo getUserInfo() {
        return userInfo;
    }
}
