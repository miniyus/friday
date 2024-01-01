package com.miniyus.friday.auth.adapter.out.persistence;

import com.miniyus.friday.auth.adapter.out.persistence.mapper.AuthMapper;
import com.miniyus.friday.auth.domain.Auth;
import com.miniyus.friday.auth.domain.Token;
import com.miniyus.friday.hexagonal.adapter.PersistenceTest;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.security.social.SocialProvider;
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


class AuthAdapterTest extends PersistenceTest<Long, UserEntity> {
    @Mock
    private CustomUserDetailsService userDetailsService;

    @Mock
    private JwtService jwtService;

    @Mock
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Mock
    private AuthMapper authMapper;

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
            .provider(SocialProvider.NONE)
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
    void signupTest() {
        var domain = Auth.builder()
            .id(1L)
            .email(testUser.getEmail())
            .name(testUser.getName())
            .password(testUser.getPassword())
            .role(testUser.getRole())
            .build();

        when(authMapper.toAuthDomain(any()))
            .thenReturn(domain);

        var userInfo = authAdapter.signup(domain);

        assertThat(userInfo)
            .hasFieldOrPropertyWithValue("id", testUser.getId())
            .hasFieldOrPropertyWithValue("email", testUser.getUsername())
            .hasFieldOrPropertyWithValue("name", testUser.getName())
            .hasFieldOrPropertyWithValue("role", testUser.getRole());
    }

    @Test
    void refreshTest() {
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
                    userEntity.setId(1L))
            );

        when(authMapper.toTokenDomain(any()))
            .thenReturn(Token.builder()
                .tokenType(JwtProvider.BEARER)
                .accessToken(faker.internet().uuidv3())
                .refreshToken(faker.internet().uuidv3())
                .expiresIn(3600L)
                .build());

        var testRefresh = jwtProvider.createRefreshToken();

        var issueToken = authAdapter.refreshToken(JwtProvider.BEARER + " " + testRefresh);
        assertThat(issueToken)
            .isNotNull()
            .hasFieldOrProperty("accessToken")
            .hasFieldOrProperty("refreshToken");
    }

    @Override
    protected UserEntity createTestEntity(int index) {
        return null;
    }
}
