package com.miniyus.friday.infrastructure.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/01
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalUserDetailsService implements CustomUserDetailsService {
    private final UserRepository userRepository;

    private PasswordEncoder passwordEncoder;


    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity) {
        return PrincipalUserInfo.builder()
                .id(entity.getId())
                .snsId(entity.getSnsId())
                .username(entity.getEmail())
                .name(entity.getName())
                .password(passwordEncoder.encode(entity.getPassword()))
                .enabled(entity.getDeletedAt() == null)
                .accountNonExpired(entity.getDeletedAt() == null)
                .accountNonLocked(entity.getDeletedAt() == null)
                .credentialsNonExpired(entity.getDeletedAt() == null)
                .attributes(null)
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

    @Override
    public PrincipalUserInfo create(PasswordUserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
                .snsId(null)
                .provider(null)
                .email(userInfo.email())
                .password(userInfo.password())
                .name(userInfo.name())
                .role(userInfo.role())
                .build();
        entity = userRepository.save(entity);

        return buildPrincipalUserInfo(entity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username).orElse(null);
        if(entity == null) {
            throw new UsernameNotFoundException("");
        }
        return buildPrincipalUserInfo(entity);
    }

    public void setPasswordEncoder(
        PasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }
}
