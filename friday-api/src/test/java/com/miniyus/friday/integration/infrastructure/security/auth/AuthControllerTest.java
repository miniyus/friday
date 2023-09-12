package com.miniyus.friday.integration.infrastructure.security.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;
import com.miniyus.friday.infrastructure.security.CustomUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserInfo;
import com.miniyus.friday.infrastructure.security.auth.AuthController;
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

/**
 * auth controller test
 *
 * @author miniyus
 * @date 2023/09/08
 */
@WebMvcTest(controllers = AuthController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs(
    uriScheme = "http",
    uriHost = "localhost",
    uriPort = 8080,
    outputDir = "build/generated-snippets")
public class AuthControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CustomUserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtConfiguration jwtConfiguration;

    @MockBean
    private PasswordEncoder passwordEncoder;

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

        when(passwordEncoder.encode(request.getPassword())).thenReturn("password@1234");

        var testAuthority = new ArrayList<GrantedAuthority>();
        testAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));
        when(userDetailsService.create(any(PasswordUserInfo.class))).thenReturn(
            PrincipalUserInfo.builder()
                .id(1L)
                .snsId(null)
                .username(request.getEmail())
                .name(request.getName())
                .password(request.getPassword())
                .enabled(true)
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .attributes(null)
                .provider(null)
                .authorities(testAuthority)
                .role(request.getRole())
                .build());
        var tokens = new IssueToken("access", 3600L, "refresh");
        when(jwtService.issueToken(1L)).thenReturn(tokens);

        ResultActions result = this.mockMvc.perform(
            post("/v1/auth/signup")
                .with(csrf().asHeader())
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.username").value(request.getEmail()))
            .andExpect(jsonPath("$.name").value(request.getName()))
            .andExpect(jsonPath("$.role").value(request.getRole()))
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
        testAuthority.add(new SimpleGrantedAuthority("ROLE_USER"));

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
            .username(signinInfo.getUsername())
            .password(signinInfo.getPassword())
            .build();

        when(userDetailsService.loadUserByUsername(any())).thenReturn(
            principal
        );

        ResultActions result = this.mockMvc.perform(
            post("/v1/auth/signin")
                .with(csrf().asHeader())
                .content(objectMapper.writeValueAsString(signinInfo))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));
        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.email").value(signinInfo.getUsername()))
            .andExpect(jsonPath("$.name").value(fakeName))
            .andExpect(jsonPath("$.tokens").isNotEmpty())
            .andExpect(jsonPath("$.tokens.accessToken").isNotEmpty())
            .andExpect(jsonPath("$.tokens.expiresIn").value(3600L))
            .andExpect(jsonPath("$.tokens.refreshToken").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "auth-signup",
                getDocumentRequest(),
                getDocumentResponse(),
                requestFields(
                    fieldWithPath("username").description("name"),
                    fieldWithPath("password").description("password")
                ),
                responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("tokens").description("tokens"),
                    fieldWithPath("tokens.accessToken").description("accessToken"),
                    fieldWithPath("tokens.accessToken").description("expiresIn"),
                    fieldWithPath("tokens.accessToken").description("refreshToken")
                )));

    }
}
