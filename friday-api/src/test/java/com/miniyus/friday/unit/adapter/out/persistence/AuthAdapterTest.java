package com.miniyus.friday.unit.adapter.out.persistence;

import com.miniyus.friday.hexagonal.application.UsecaseTest;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.users.adapter.out.persistence.AuthAdapter;
import com.miniyus.friday.users.domain.Auth;
import com.miniyus.friday.users.domain.UserRole;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;


public class AuthAdapterTest extends UsecaseTest {
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
            .name(faker.name().name())
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
        var userEntity = UserEntity.create(
            testUser.getSnsId(),
            testUser.getProvider(),
            testUser.getEmail(),
            testUser.getPassword(),
            testUser.getName(),
            testUser.getRole()
        );

        when(jwtService.getUserByRefreshToken(any()))
            .thenReturn(
                Optional.of(
                    userEntity.setId(testUser.getId()))
            );

        var testRefresh = jwtProvider.createRefreshToken();

        var issueToken = authAdapter.refreshToken(JwtProvider.BEARER + " " + testRefresh);
        assertThat(issueToken)
            .isNotNull()
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("refreshToken");
    }
}
