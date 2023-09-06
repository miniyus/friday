package com.miniyus.friday.unit.infrastructure.auth;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.miniyus.friday.infrastructure.auth.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.auth.PrincipalUserService;
import com.miniyus.friday.infrastructure.auth.oauth2.userinfo.GoogleUserInfo;
import com.miniyus.friday.infrastructure.auth.oauth2.userinfo.OAuth2UserInfo;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;

/**
 * [description]
 *
 * @author seongminyoo
 * @date 2023/09/06
 */
@ExtendWith(MockitoExtension.class)
public class PrincipalUserServiceTest {
    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private PrincipalUserService principalUserService;

    private UserEntity testUser;

    @BeforeEach
    void setUp() {
        testUser = UserEntity.builder()
                .snsId("test1234")
                .email("miniyus@gmail.com")
                .name("smyoo")
                .provider("google")
                .build();
    }

    @Test
    void createOAuth2User() {
        OAuth2UserInfo userInfo = GoogleUserInfo.builder()
                .snsId("test1234")
                .email("miniyu97@gmail.com")
                .name("smyoo")
                .attributes(null)
                .build();

        PrincipalUserInfo principal = principalUserService.create(userInfo);

    }
}
