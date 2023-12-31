package com.meteormin.friday.auth.adapter.in.rest;

import com.meteormin.friday.annotation.WithMockCustomUser;
import com.meteormin.friday.api.auth.resource.AuthUserResource;
import com.meteormin.friday.auth.AuthDocument;
import com.meteormin.friday.auth.application.port.in.query.RetrieveUserInfoQuery;
import com.meteormin.friday.auth.application.port.in.usecase.AuthUsecase;
import com.meteormin.friday.auth.domain.Auth;
import com.meteormin.friday.auth.domain.Token;
import com.meteormin.friday.infrastructure.jwt.IssueToken;
import com.meteormin.friday.infrastructure.persistence.entities.UserEntity;
import com.meteormin.friday.infrastructure.security.PrincipalUserInfo;
import com.meteormin.friday.infrastructure.security.auth.PasswordAuthentication;
import com.meteormin.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import com.meteormin.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.meteormin.friday.infrastructure.security.social.SocialProvider;
import com.meteormin.friday.users.domain.UserRole;
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
class AuthControllerTest extends AuthDocument {
    @MockBean
    private AuthUsecase usecase;

    @MockBean
    private RetrieveUserInfoQuery retrieveUserInfoQuery;


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

        var response = Auth.builder()
            .id(1L)
            .name(request.name())
            .role(UserRole.MANAGER)
            .password(passwordEncoder.encode(request.password()))
            .email(request.email())
            .build();
        when(usecase.signup(any())).thenReturn(response);

        var principal = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(request.name())
            .role(UserRole.MANAGER)
            .provider(SocialProvider.NONE)
            .snsId(null)
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();
        when(userDetailsService.loadUserByUsername(any())).thenReturn(principal);

        return AuthUserResource.fromDomain(response);
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
    void signupTest() throws Exception {
        var request = buildSignupRequest();
        var response = buildSignupResponse(request);
        signup(request, response);
    }

    private PasswordAuthentication buildSigninRequest() {
        return new PasswordAuthentication(
            faker.internet().emailAddress(),
            faker.internet().password(),
            "secret"
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
            .provider(SocialProvider.NONE)
            .snsId(null)
            .email(request.email())
            .password(passwordEncoder.encode(request.password()))
            .build();

        when(userDetailsService.loadUserByUsername(any()))
            .thenReturn(response);

        var tokens = buildIssueToken();

        return new PasswordTokenResponse(
            response.getId(),
            response.getEmail(),
            response.getName(),
            tokens
        );
    }

    @Test
    void signinTest() throws Exception {
        var signinInfo = buildSigninRequest();
        var response = buildSigninResponse(signinInfo);
        signin(signinInfo, response);
    }

    private Token buildRefreshTokenResponse() {
        var issueToken = buildIssueToken();
        var user = UserEntity.builder()
            .id(1L)
            .email(faker.internet().emailAddress())
            .password(faker.internet().password())
            .name(faker.name().fullName())
            .role(UserRole.USER)
            .provider(SocialProvider.NONE)
            .build();

        when(jwtService.getUserByRefreshToken(any())).thenReturn(
            Optional.of(user));

        var token = Token.builder()
            .tokenType(issueToken.tokenType())
            .accessToken(issueToken.accessToken())
            .refreshToken(issueToken.refreshToken())
            .expiresIn(issueToken.expiresIn())
            .build();
        when(usecase.refreshToken(any())).thenReturn(
            token
        );

        return token;
    }

    @Test
    @WithMockCustomUser
    void refreshTest() throws Exception {
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
            .provider(SocialProvider.GOOGLE)
            .snsId(faker.internet().uuid())
            .email(faker.internet().emailAddress())
            .password(passwordEncoder.encode(faker.internet().password()))
            .build();

        var domain = Auth.builder()
            .email(principal.getEmail())
            .id(principal.getId())
            .name(principal.getName())
            .snsId(principal.getSnsId())
            .snsId(principal.getName())
            .role(principal.getRole())
            .build();
        when(retrieveUserInfoQuery.retrieveUserInfo()).thenReturn(domain);
        return AuthUserResource.fromDomain(domain);
    }

    @Test
    @WithMockCustomUser
    void userInfoTest() throws Exception {
        var response = buildUserInfoResponse();
        userInfo(response);
    }

    @Test
    @WithMockCustomUser
    void logoutTest() throws Exception {
        logout();
    }
}
