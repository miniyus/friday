package com.miniyus.friday.integration.auth;

import com.github.javafaker.Faker;
import com.miniyus.friday.auth.AuthController;
import com.miniyus.friday.auth.AuthService;
import com.miniyus.friday.infrastructure.jpa.entities.UserEntity;
import com.miniyus.friday.infrastructure.security.PrincipalUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserService;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import com.miniyus.friday.infrastructure.security.config.SecurityConfiguration;
import com.miniyus.friday.infrastructure.security.oauth2.handler.OAuth2AccessDeniedHandler;
import com.miniyus.friday.infrastructure.security.oauth2.handler.OAuth2AuthenticationEntryPoint;
import com.miniyus.friday.infrastructure.security.oauth2.handler.OAuth2FailureHandler;
import com.miniyus.friday.infrastructure.security.oauth2.handler.OAuth2SuccessHandler;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;

import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentRequest;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.ArrayList;
import java.util.Optional;

/**
 * auth controller test
 *
 * @author miniyus
 * @date 2023/09/08
 */
@WebMvcTest(controllers = AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@Import(SecurityConfiguration.class)
@ActiveProfiles("test")
@AutoConfigureRestDocs(
    outputDir = "build/generated-snippets")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PrincipalUserDetailsService userDetailsService;

    @MockBean
    private PrincipalUserService userService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtConfiguration jwtConfiguration;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @MockBean
    private OAuth2SuccessHandler oauth2SuccessHandler;

    @MockBean
    private OAuth2FailureHandler oauth2FailureHandler;

    @MockBean
    private OAuth2AuthenticationEntryPoint oauth2AuthenticationEntryPoint;

    @MockBean
    private OAuth2AccessDeniedHandler oauth2AccessDeniedHandler;

    @MockBean
    private AuthService authService;

    @Test
    @WithMockCustomUser
    public void signupTest() throws Exception {
        PasswordUserInfo request = PasswordUserInfo
            .builder()
            .email("testser@gmail.com")
            .name("tester")
            .password("password@1234")
            .role("USER")
            .build();

        //        when(passwordEncoder.encode(request.password())).thenReturn("password@1234");

        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));

        var returnUserInfo = PrincipalUserInfo.builder()
                .id(1L)
                .snsId(null)
                .username(request.email())
                .name(request.name())
                .password(request.password())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .attributes(null)
                .provider(null)
                .authorities(testAuthority)
                .role(request.role())
                .build();

        when(userDetailsService.create(any(PasswordUserInfo.class))).thenReturn(returnUserInfo);
        var tokens = new IssueToken(
            "Bearer",
            "accessToken",
            "access",
            3600L,
            "refreshToken",
            "refresh");
        when(jwtService.issueToken(1L)).thenReturn(tokens);

        when(authService.signup(any())).thenReturn(returnUserInfo);

        ResultActions result = this.mockMvc.perform(
            post("/v1/auth/signup")
                .with(csrf().asHeader())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value(request.email()))
            .andExpect(jsonPath("$.name").value(request.name()))
            .andExpect(jsonPath("$.role").value(request.role()))
            .andDo(MockMvcRestDocumentation.document(
                "auth-signup",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("password").description("password"),
                    fieldWithPath("role").description("role")),
                responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("snsId").description("sns id"),
                    fieldWithPath("username").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("enabled").description("enabled"),
                    fieldWithPath("snsUser").description("is sns user"),
                    fieldWithPath("accountNonExpired").description("accountNonExpired"),
                    fieldWithPath("credentialsNonExpired")
                        .description("credentialsNonExpired"),
                    fieldWithPath("accountNonLocked").description("accountNonLocked"),
                    fieldWithPath("attributes").description("attributes"),
                    fieldWithPath("authorities").description("authorities"),
                    fieldWithPath("authorities[].authority")
                        .description("authority"))));
    }

    @Test
    public void signinTest() throws Exception {
        var faker = new Faker();
        var signinInfo = new PasswordAuthentication(
            faker.internet().emailAddress(),
            faker.internet().password());

        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("USER"));

        var fakeName = faker.name().fullName();

        var principal = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(fakeName)
            .role("USER")
            .provider(null)
            .snsId(null)
            .username(signinInfo.username())
            .password(passwordEncoder.encode(signinInfo.password()))
            .build();

        when(userDetailsService.loadUserByUsername(any())).thenReturn(
            principal);
        var tokens = new IssueToken(
            "Bearer",
            "Authorization",
            "{accessToken}",
            3600L,
            "RefreshToken",
            "{refreshToken}"
        );

        when(jwtService.issueToken(any(Long.class))).thenReturn(tokens);

        ResultActions result = this.mockMvc.perform(
            post("/v1/auth/signin")
                .with(csrf().asHeader())
                .content(objectMapper.writeValueAsString(signinInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value(signinInfo.username()))
            .andExpect(jsonPath("$.name").value(fakeName))
            .andExpect(jsonPath("$.tokens").isNotEmpty())
            .andExpect(jsonPath("$.tokens.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.tokens.expiresIn").value(3600L))
            .andExpect(jsonPath("$.tokens.refreshToken").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "auth-signin",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("username").description("name"),
                    fieldWithPath("password").description("password")),
                responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("tokens").description("tokens"),
                    fieldWithPath("tokens.tokenType").description("tokenType"),
                    fieldWithPath("tokens.accessToken").description("accessToken"),
                    fieldWithPath("tokens.expiresIn").description("expiresIn"),
                    fieldWithPath("tokens.refreshToken").description("refreshToken"))));

    }

    @Test
    @WithMockCustomUser
    public void refreshTest() throws Exception {
        var faker = new Faker();
        var fakeAccessToken = faker.internet().uuid();
        var fakeRefreshToken = faker.internet().uuid();
        var tokens = new IssueToken(
            "Bearer",
            "accessToken",
            fakeAccessToken,
            3600L,
            "refreshToken",
            fakeRefreshToken);

        when(jwtService.issueToken(any(Long.class))).thenReturn(
            tokens);

        var user = new UserEntity(
            1L,
            null,
            null,
            faker.internet().emailAddress(),
            faker.internet().password(),
            faker.name().fullName(),
            "USER",
            null);

        when(jwtService.getUserByRefreshToken(any())).thenReturn(
            Optional.of(user));

        when(authService.refresh(any())).thenReturn(tokens);

        var result = this.mockMvc.perform(
            post("/v1/auth/refresh")
                .with(csrf())
                .header("RefreshToken",fakeRefreshToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.tokenType").value("Bearer"))
            .andExpect(jsonPath("$.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.expiresIn").value(3600L))
            .andExpect(jsonPath("$.refreshToken").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "auth-refresh",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                    fieldWithPath("tokenType").description("tokenType"),
                    fieldWithPath("accessToken").description("accessToken"),
                    fieldWithPath("expiresIn").description("expiresIn"),
                    fieldWithPath("refreshToken").description("refreshToken"))));
    }

    @Test
    @WithMockCustomUser
    public void userInfoTest() throws Exception {
        var faker = new Faker();
        var signinInfo = new PasswordAuthentication(
            faker.internet().emailAddress(),
            faker.internet().password());

        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("USER"));

        var fakeName = faker.name().fullName();

        var principal = PrincipalUserInfo.builder()
            .id(1L)
            .accountNonExpired(true)
            .accountNonLocked(true)
            .attributes(null)
            .authorities(testAuthority)
            .credentialsNonExpired(true)
            .enabled(true)
            .name(fakeName)
            .role("USER")
            .provider(null)
            .snsId(null)
            .username(signinInfo.username())
            .password(passwordEncoder.encode(signinInfo.password()))
            .build();

        when(authService.userInfo()).thenReturn(principal);
        var result = this.mockMvc.perform(
            get("/v1/auth/me")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").isNotEmpty())
            .andExpect(jsonPath("$.name").isNotEmpty())
            .andExpect(jsonPath("$.role").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "auth-userInfo",
                getDocumentRequest(),
                getDocumentResponse(),
                responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("snsId").description("sns id"),
                    fieldWithPath("username").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("enabled").description("enabled"),
                    fieldWithPath("snsUser").description("is sns user"),
                    fieldWithPath("accountNonExpired").description("accountNonExpired"),
                    fieldWithPath("credentialsNonExpired")
                        .description("credentialsNonExpired"),
                    fieldWithPath("accountNonLocked").description("accountNonLocked"),
                    fieldWithPath("attributes").description("attributes"),
                    fieldWithPath("authorities").description("authorities"),
                    fieldWithPath("authorities[].authority")
                        .description("authority")
                )
            ));
    }

    @Test
    @WithMockCustomUser
    public void logoutTest() throws Exception {
        var result = this.mockMvc.perform(
            post("/v1/auth/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent())
            .andDo(MockMvcRestDocumentation.document(
                "auth-logout",
                getDocumentRequest(),
                getDocumentResponse()
            ));
    }
}
