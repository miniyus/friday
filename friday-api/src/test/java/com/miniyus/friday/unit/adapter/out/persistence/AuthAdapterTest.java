package com.miniyus.friday.unit.adapter.out.persistence;

import com.github.javafaker.Faker;
import com.miniyus.friday.adapter.out.persistence.AuthAdapter;
import com.miniyus.friday.domain.auth.Auth;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.common.UserRole;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthAdapterTest {
    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    private final Faker faker = new Faker();

    @InjectMocks
    private AuthAdapter authAdapter;

    private PrincipalUserInfo testUser;

    private JwtProvider jwtProvider;

    @BeforeEach
    public void setup() {
        testUser = PrincipalUserInfo.builder()
            .id(1L)
            .email(faker.internet().safeEmailAddress())
            .password(faker.internet().password())
            .name(faker.name().username())
            .role(UserRole.USER)
            .build();


        jwtProvider = new JwtProvider(
            faker.internet().uuid(),
            3600L,
            360000L,
            "Authorization",
            "RefreshToken"
        );

        var testAccess = jwtProvider.createAccessToken(testUser.getUsername());
        var testRefresh = jwtProvider.createRefreshToken();

        lenient().when(jwtService.issueToken(any(Long.class)))
            .thenReturn(
                new IssueToken(
                    JwtProvider.BEARER,
                    jwtProvider.accessTokenKey(),
                    testAccess,
                    jwtProvider.accessTokenExpiration(),
                    jwtProvider.refreshTokenKey(),
                    testRefresh
                )
            );
        lenient().when(userDetailsService.create(any()))
            .thenReturn(
                testUser
            );
        lenient().when(userDetailsService.loadUserByUsername(any()))
            .thenReturn(
                testUser
            );

    }

    @Test
    public void signupTest() {
        var passwordUserInfo = new PasswordUserInfo(
            testUser.getUsername(),
            testUser.getPassword(),
            testUser.getName()
        );

        var userInfo = authAdapter.signup(
            Auth.builder()
                .email(testUser.getEmail())
                .name(testUser.getName())
                .password(testUser.getPassword())
                .build()
        );

        assertThat(userInfo)
            .hasFieldOrPropertyWithValue("id", testUser.getId())
            .hasFieldOrPropertyWithValue("email", testUser.getUsername())
            .hasFieldOrPropertyWithValue("name", testUser.getName())
            .hasFieldOrPropertyWithValue("role", testUser.getRole());
    }

    @Test
    public void refreshTest() {
        when(jwtService.getUserByRefreshToken(any()))
            .thenReturn(
                Optional.of(
                    new UserEntity(
                        testUser.getId(),
                        null,
                        null,
                        testUser.getUsername(),
                        testUser.getPassword(),
                        testUser.getName(),
                        testUser.getRole(),
                        null
                    )
                )
            );

        var testRefresh = jwtProvider.createRefreshToken();

        var issueToken = authAdapter.refreshToken(JwtProvider.BEARER + " " + testRefresh);
        assertThat(issueToken)
            .isNotNull()
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("refreshToken");
    }
}
