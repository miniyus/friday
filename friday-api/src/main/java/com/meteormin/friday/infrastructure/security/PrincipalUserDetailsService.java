package com.meteormin.friday.infrastructure.security;

import com.meteormin.friday.infrastructure.persistence.entities.UserEntity;
import com.meteormin.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.meteormin.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.meteormin.friday.infrastructure.security.social.SocialProvider;
import com.meteormin.friday.users.domain.UserRole;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;

/**
 * [description]
 *
 * @author meteormin
 * @since 2023/09/01
 */
@Service
@Transactional
@RequiredArgsConstructor
public class PrincipalUserDetailsService implements CustomUserDetailsService {
    private final UserEntityRepository userRepository;

    @Setter
    private PasswordEncoder passwordEncoder;

    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity) {
        return PrincipalUserInfo.builder()
            .id(entity.getId())
            .snsId(entity.getSnsId())
            .email(entity.getEmail())
            .name(entity.getName())
            .password(entity.getPassword())
            .enabled(entity.getDeletedAt() == null)
            .accountNonExpired(entity.getDeletedAt() == null)
            .accountNonLocked(entity.getDeletedAt() == null)
            .credentialsNonExpired(entity.getDeletedAt() == null)
            .attributes(null)
            .provider(SocialProvider.NONE)
            .authorities(getAuthorities(entity))
            .role(entity.getRole())
            .build();
    }

    private Collection<? extends GrantedAuthority> getAuthorities(UserEntity entity) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();

        authorities.add((GrantedAuthority) () -> entity.getRole().value());

        return authorities;
    }

    @Override
    public PrincipalUserInfo create(PasswordUserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
            .snsId(null)
            .provider(null)
            .email(userInfo.email())
            .password(passwordEncoder.encode(userInfo.password()))
            .name(userInfo.name())
            .role(UserRole.USER)
            .build();

        return buildPrincipalUserInfo(
            userRepository.save(entity)
        );
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity entity = userRepository.findByEmail(username).orElse(null);
        if (entity == null) {
            throw new UsernameNotFoundException("");
        }
        return buildPrincipalUserInfo(entity);
    }
}
