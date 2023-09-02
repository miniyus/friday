package com.miniyus.friday.infrastructure.oauth2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.infrastructure.oauth2.userinfo.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
@Service
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;

    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity, Map<String, Object> attributes) {
        return PrincipalUserInfo.builder()
                .username(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .enabled(entity.getDeletedAt() == null)
                .accountNonExpired(entity.getDeletedAt() == null)
                .accountNonLocked(entity.getDeletedAt() == null)
                .attributes(attributes)
                .authorities(getAuthorities(entity))
                .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity entity) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return entity.getRole();
            }
        });

        return authorities;
    };

    public PrincipalUserInfo loadUser(OAuth2UserInfo userInfo) {
        UserEntity entity = userRepository.findByEmail(userInfo.getEmail());
        return buildPrincipalUserInfo(entity, userInfo.getAttributes());
    }

    public PrincipalUserInfo create(OAuth2UserInfo userInfo) {
        UserEntity entity = new UserEntity(
                null,
                userInfo.getSnsId(),
                userInfo.getProvider().getId(),
                userInfo.getEmail(),
                null,
                userInfo.getName(),
                UserRole.USER.getValue(),
                null,
                null,
                null,
                null);

        entity = userRepository.save(entity);

        return buildPrincipalUserInfo(entity, userInfo.getAttributes());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username);
        return buildPrincipalUserInfo(entity, null);
    }
}
