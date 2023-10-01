package com.miniyus.friday.integration.adapter.in.rest;

import com.miniyus.friday.adapter.in.rest.AuthController;
import com.miniyus.friday.adapter.in.rest.resource.UserResources.*;
import com.miniyus.friday.adapter.out.persistence.AuthAdapter;
import com.miniyus.friday.common.UserRole;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import com.miniyus.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.security.oauth2.OAuth2Provider;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import com.miniyus.friday.integration.document.AuthDocument;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(controllers = AuthController.class)
public class AuthControllerTest extends AuthDocument {
    @MockBean
    private AuthAdapter authAdapter;

    private PasswordUserInfo buildSignupRequest() {
        return PasswordUserInfo
            .builder()
            .email(faker.internet().safeEmailAddress())
            .name(faker.name().fullName())
            .password(faker.internet().password())
            .build();
    }

     private AuthUserResource buildSignupResponse(PasswordUserInfo request) {
        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));

        var response = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(request.name())
            .role(UserRole.MANAGER)
            .provider(null)
            .snsId(null)
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

        when(authAdapter.signup(any(PasswordUserInfo.class))).thenReturn(response);
        when(userDetailsService.loadUserByUsername(any())).thenReturn(response);
        return AuthUserResource.fromPrincipalUserInfo(response);
    }

    private IssueToken buildIssueToken() {
        var tokens = new IssueToken(
            "Bearer",
            faker.internet().uuid(),
            "access",
            3600L,
            faker.internet().uuid(),
            "refresh");
        when(jwtService.issueToken(any(Long.class))).thenReturn(tokens);
        return tokens;
    }

    @Test
    @WithMockCustomUser(role = UserRole.ADMIN)
    public void signupTest() throws Exception {
        var request = buildSignupRequest();
        var response = buildSignupResponse(request);
        signup(request, response);
    }

    private PasswordAuthentication buildSigninRequest() {
        return new PasswordAuthentication(
            faker.internet().emailAddress(),
            faker.internet().password()
        );
    }

    private PasswordTokenResponse buildSigninResponse(PasswordAuthentication request) {
        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));

        var response = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(faker.name().fullName())
            .role(UserRole.MANAGER)
            .provider(null)
            .snsId(null)
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

        when(userDetailsService.loadUserByUsername(any()))
            .thenReturn(response);

        var resource = AuthUserResource.fromPrincipalUserInfo(response);
        var tokens = buildIssueToken();

        return new PasswordTokenResponse(
            resource.id(),
            resource.email(),
            resource.name(),
            tokens
        );
    }

    @Test
    public void signinTest() throws Exception {
        var signinInfo = buildSigninRequest();
        var response = buildSigninResponse(signinInfo);
        signin(signinInfo, response);
    }

    private IssueToken buildRefreshTokenResponse() {
        var tokens = buildIssueToken();
        var user = UserEntity.builder()
            .id(1L)
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .name(faker.name().fullName())
            .role(UserRole.USER)
            .build();

        when(jwtService.getUserByRefreshToken(any())).thenReturn(
            Optional.of(user));

        when(authAdapter.refresh(any())).thenReturn(tokens);

        return tokens;
    }

    @Test
    @WithMockCustomUser
    public void refreshTest() throws Exception {
        var tokens = buildIssueToken();
        refresh(tokens.refreshToken(), buildRefreshTokenResponse());
    }

    private AuthUserResource buildUserInfoResponse() {
        var testAuthority = new ArrayList<GrantedAuthority>();

        testAuthority.add(new SimpleGrantedAuthority("USER"));

        var principal = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(faker.name().fullName())
            .role(UserRole.USER)
            .provider(OAuth2Provider.GOOGLE)
            .snsId(faker.internet().uuid())
            .email(faker.internet().emailAddress())
            .password(passwordEncoder.encode(faker.internet().password()))
            .build();

        when(authAdapter.userInfo()).thenReturn(principal);
        return AuthUserResource.fromPrincipalUserInfo(principal);
    }

    @Test
    @WithMockCustomUser
    public void userInfoTest() throws Exception {
        var response = buildUserInfoResponse();
        userInfo(response);
    }

    @Test
    @WithMockCustomUser
    public void logoutTest() throws Exception {
        logout();
    }
}
