package com.miniyus.friday.infrastructure.security;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import com.miniyus.friday.common.error.AuthErrorCode;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserRepository;
import com.miniyus.friday.infrastructure.security.oauth2.OAuth2Provider;
import com.miniyus.friday.infrastructure.security.oauth2.userinfo.OAuth2Attributes;
import com.miniyus.friday.infrastructure.security.oauth2.userinfo.OAuth2UserInfo;
import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/27
 */
@Service
@RequiredArgsConstructor
@Transactional
public class PrincipalUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    private PrincipalUserInfo buildPrincipalUserInfo(UserEntity entity,
        Map<String, Object> attributes) {
        OAuth2Provider provider = null;
        if (entity.getProvider() != null) {
            provider = OAuth2Provider.of(entity.getProvider());
        }

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
            .attributes(attributes)
            .provider(provider)
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
    }

    ;

    public PrincipalUserInfo create(OAuth2UserInfo userInfo) {
        UserEntity entity = UserEntity.builder()
            .snsId(userInfo.snsId())
            .provider(userInfo.getProvider().getId())
            .email(userInfo.email())
            .password(null)
            .name(userInfo.name())
            .role(UserRole.USER.getValue())
            .build();
        entity = userRepository.save(entity);

        return buildPrincipalUserInfo(entity, userInfo.attributes());
    }

    /**
     * Loads the OAuth2User for the given OAuth2UserRequest.
     *
     * @param userRequest the OAuth2UserRequest object containing the request details
     * @return the OAuth2User representing the loaded user
     * @throws OAuth2AuthenticationException if an error occurs during the authentication process
     */
    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        String registrationId = userRequest.getClientRegistration()
            .getRegistrationId();
        String userNameAttributeName = userRequest.getClientRegistration()
            .getProviderDetails()
            .getUserInfoEndpoint()
            .getUserNameAttributeName();

        OAuth2Attributes oAuthAttributes =
            OAuth2Attributes.of(registrationId, userNameAttributeName,
                oAuth2User.getAttributes());

        OAuth2UserInfo userInfo = oAuthAttributes.toUserInfo();
        UserEntity userREntity = userRepository
            .findBySnsIdAndProvider(userInfo.snsId(), userInfo.getProvider().getId())
            .orElseThrow(() -> new OAuth2AuthenticationException(AuthErrorCode.INVALID_REQUEST.name()));

        PrincipalUserInfo user = buildPrincipalUserInfo(
            userREntity,
            oAuthAttributes.getAttributes()
        );
        if (user == null) {
            user = create(userInfo);
        }

        return user;
    }

}
