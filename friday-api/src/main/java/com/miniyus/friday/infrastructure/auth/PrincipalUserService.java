package com.miniyus.friday.infrastructure.auth;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.miniyus.friday.infrastructure.auth.oauth2.userinfo.OAuth2Attributes;
import com.miniyus.friday.infrastructure.auth.oauth2.userinfo.OAuth2UserInfo;

import lombok.RequiredArgsConstructor;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/08/27
 */
@Service
@RequiredArgsConstructor
public class PrincipalUserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	private final PrincipalUserDetailsService userService;

	/**
	 * Loads the OAuth2User for the given OAuth2UserRequest.
	 *
	 * @param userRequest the OAuth2UserRequest object containing the request
	 *                    details
	 * @return the OAuth2User representing the loaded user
	 * @throws OAuth2AuthenticationException if an error occurs during the
	 *                                       authentication process
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

		OAuth2Attributes oAuthAttributes = OAuth2Attributes.of(registrationId, userNameAttributeName,
				oAuth2User.getAttributes());

		OAuth2UserInfo userInfo = oAuthAttributes.toUserInfo();
		PrincipalUserInfo user = userService.loadUser(userInfo);
		if (user == null) {
			user = userService.create(userInfo);
		}

		return user;
	}

}
