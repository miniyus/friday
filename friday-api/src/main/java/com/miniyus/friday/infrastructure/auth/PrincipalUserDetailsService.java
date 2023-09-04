package com.miniyus.friday.infrastructure.auth;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
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

    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity, Map<String, Object> attributes) {
        return PrincipalUserInfo.builder()
                .id(entity.getId())
                .snsId(entity.getSnsId())
                .username(entity.getEmail())
                .name(entity.getName())
                .password(entity.getPassword())
                .enabled(entity.getDeletedAt() == null)
                .accountNonExpired(entity.getDeletedAt() == null)
                .accountNonLocked(entity.getDeletedAt() == null)
                .credentialsNonExpired(entity.getDeletedAt() == null)
                .attributes(attributes)
                .provider(null)
                .authorities(getAuthorities(entity))
                .role(entity.getRole())
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

    public PrincipalUserInfo create(PasswordUserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
                .snsId(null)
                .provider(null)
                .email(userInfo.getEmail())
                .password(userInfo.getPassword())
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
}
