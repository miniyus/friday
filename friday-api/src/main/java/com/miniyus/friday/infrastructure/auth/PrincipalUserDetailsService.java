package com.miniyus.friday.infrastructure.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.miniyus.friday.common.error.AuthErrorCode;
import com.miniyus.friday.common.error.AuthErrorException;
import com.miniyus.friday.infrastructure.auth.oauth2.OAuth2Provider;
import com.miniyus.friday.infrastructure.auth.oauth2.userinfo.OAuth2UserInfo;
import com.miniyus.friday.infrastructure.auth.login.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;

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
    private final PasswordEncoder passwordEncoder;

    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity, Map<String, Object> attributes) {
        OAuth2Provider provider = null;
        if (entity.getProvider() != null) {
            provider = OAuth2Provider.of(entity.getProvider());
        }

        return PrincipalUserInfo.builder()
                .username(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .enabled(entity.getDeletedAt() == null)
                .accountNonExpired(entity.getDeletedAt() == null)
                .accountNonLocked(entity.getDeletedAt() == null)
                .attributes(attributes)
                .provider(provider)
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
        UserEntity entity = userRepository.findByEmail(userInfo.getEmail()).get();
        return buildPrincipalUserInfo(entity, userInfo.getAttributes());
    }

    public PrincipalUserInfo create(OAuth2UserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
                .snsId(userInfo.getSnsId())
                .provider(userInfo.getProvider().getId())
                .email(userInfo.getEmail())
                .password(null)
                .name(userInfo.getName())
                .role(UserRole.USER.getValue())
                .build();
        entity = userRepository.save(entity);

        return buildPrincipalUserInfo(entity, userInfo.getAttributes());
    }

    public PrincipalUserInfo create(PasswordUserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
                .snsId(null)
                .provider(null)
                .email(userInfo.getEmail())
                .password(passwordEncoder.encode(userInfo.getPassword()))
                .name(userInfo.getName())
                .role(userInfo.getRole())
                .build();
        entity = userRepository.save(entity);

        return buildPrincipalUserInfo(entity, null);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username).get();
        return buildPrincipalUserInfo(entity, null);
    }

    public PrincipalUserInfo passwordAuthentication(String username, String password) {
        UserEntity entity = userRepository.findByEmail(username).get();
        boolean isPass = passwordEncoder.matches(password, entity.getPassword());
        if (isPass) {
            return buildPrincipalUserInfo(entity, null);
        }

        throw new AuthErrorException("password not matchs", AuthErrorCode.INVALID_REQUEST);
    }
}
