package com.miniyus.friday.integration.api.users.adapter.in.rest;

import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentRequest;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.requestHeaders;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.api.users.application.port.in.UserResource;
import com.miniyus.friday.api.users.application.port.in.usecase.*;
import com.miniyus.friday.infrastructure.security.UserRole;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import com.miniyus.friday.api.users.adapter.in.rest.UserController;
import com.miniyus.friday.api.users.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.api.users.domain.User;

import java.time.LocalDateTime;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * user controller test
 *
 * @author miniyus
 * @date 2023/09/07
 */
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs(
    uriScheme = "http",
    uriHost = "localhost",
    uriPort = 8080,
    outputDir = "build/generated-snippets")
public class UserControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateUserUsecase createUserUsecase;

    @MockBean
    private RetrieveUserQuery retrieveUserQuery;

    @MockBean
    private UpdateUserUsecase updateUserUsecase;

    @MockBean
    private DeleteUserUsecase deleteUserUsecase;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {

    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    public void createUserTest() throws Exception {
        User domain = new User(
            1L,
            "tester@gmail.com",
            passwordEncoder.encode("password@1234"),
            "tester",
            "USER",
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null);

        when(createUserUsecase.createUser(any(CreateUserRequest.class))).thenReturn(
            UserResource.fromDomain(domain));

        UserResource response = UserResource.fromDomain(domain);

        var request = CreateUserRequest.builder().email("miniyu97@gmail.com")
            .name("tester").password("password@1234")
            .role(UserRole.USER.getValue()).build();

        ResultActions result = this.mockMvc.perform(post("/v1/users")
            .with(csrf().asHeader())
            .header("Authorization", "Bearer {access-token}")
            .content(objectMapper.writeValueAsString(request))
            .contentType(MediaType.APPLICATION_JSON)
            .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(response.id()))
            .andExpect(jsonPath("$.email").value(response.email()))
            .andExpect(jsonPath("$.name").value(response.name()))
            .andExpect(jsonPath("$.role").value(response.role()))
            .andExpect(jsonPath("$.snsId").value(response.snsId()))
            .andExpect(jsonPath("$.provider").value(response.provider()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "create-user",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰")),
                requestFields(
                    fieldWithPath("email")
                        .description("이메일"),
                    fieldWithPath("name")
                        .description("이름"),
                    fieldWithPath("password")
                        .description("비밀번호"),
                    fieldWithPath("role").description(
                        "역할")),
                responseFields(
                    fieldWithPath("id").description(
                        "user identifier"),
                    fieldWithPath("email").description(
                        "email"),
                    fieldWithPath("name").description(
                        "name"),
                    fieldWithPath("role").description(
                        "role"),
                    fieldWithPath("snsId").description(
                        "snsId"),
                    fieldWithPath("provider")
                        .description("provider"),
                    fieldWithPath("createdAt")
                        .description("createdAt"),
                    fieldWithPath("updatedAt")
                        .description("updatedAt"))));
    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    void retrieveUserTest() throws Exception {
        User domain = new User(
            1L,
            "tester@gmail.com",
            "password@1234",
            "tester",
            "USER",
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null);

        when(retrieveUserQuery.findById(1L)).thenReturn(UserResource.fromDomain(domain));

        var result = mockMvc.perform(
            get("/v1/users/1")
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(domain.getId()))
            .andExpect(jsonPath("$.name").value(domain.getName()))
            .andExpect(jsonPath("$.email").value(domain.getEmail()))
            .andExpect(jsonPath("$.role").value(domain.getRole()))
            .andExpect(jsonPath("$.snsId").value(domain.getSnsId()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andDo(MockMvcRestDocumentation.document(
                "retrieve-user",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰")),
                responseFields(
                    fieldWithPath("id").description(
                        "user identifier"),
                    fieldWithPath("email").description(
                        "email"),
                    fieldWithPath("name").description(
                        "name"),
                    fieldWithPath("role").description(
                        "role"),
                    fieldWithPath("snsId").description(
                        "snsId"),
                    fieldWithPath("provider")
                        .description("provider"),
                    fieldWithPath("createdAt")
                        .description("createdAt"),
                    fieldWithPath("updatedAt")
                        .description("updatedAt"))));
    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    public void updateUserTest() throws Exception {
        User domain = new User(
            1L,
            "tester@gmail.com",
            "password@1234",
            "tester",
            "USER",
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null);

        // when(retrieveUserQuery.findById(1L)).thenReturn(domain);

        domain.patch("updateName", null);

        when(updateUserUsecase.patchUser(any(), any(UpdateUserRequest.class))).thenReturn(
            UserResource.fromDomain(domain));

        var request = UpdateUserRequest.builder()
            .name("updateName")
            .role("USER")
            .build();

        var result = mockMvc.perform(
            patch("/v1/users/1")
                .content(objectMapper.writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}"));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(domain.getId()))
            .andExpect(jsonPath("$.name").value("updateName"))
            .andExpect(jsonPath("$.role").value(domain.getRole()))
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.snsId").value(domain.getSnsId()))
            .andExpect(jsonPath("$.provider").value(domain.getProvider()))
            .andExpect(jsonPath("$.email").value(domain.getEmail()))
            .andDo(MockMvcRestDocumentation.document(
                "update-user",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰")),
                requestFields(
                    fieldWithPath("name").description(
                        "name"),
                    fieldWithPath("role").description(
                        "role")),
                responseFields(
                    fieldWithPath("id").description(
                        "user identifier"),
                    fieldWithPath("name").description(
                        "name"),
                    fieldWithPath("role").description(
                        "role"),
                    fieldWithPath("updatedAt")
                        .description("updatedAt"),
                    fieldWithPath("createdAt")
                        .description("createdAt"),
                    fieldWithPath("snsId").description(
                        "snsId"),
                    fieldWithPath("provider")
                        .description("provider"),
                    fieldWithPath("email").description(
                        "email"))));
    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    public void restPasswordTest() throws Exception {
        User domain = new User(
            1L,
            "tester@gmail.com",
            "password@1234",
            "tester",
            "USER",
            null,
            null,
            LocalDateTime.now(),
            LocalDateTime.now(),
            null);

        // when(retrieveUserQuery.findById(1L)).thenReturn(domain);

        domain.resetPassword("resetPassword");

        when(updateUserUsecase.resetPassword(1L, "resetPassword"))
            .thenReturn(
                UserResource.fromDomain(domain)
            );

        var request = new ResetPasswordRequest("resetPassword");

        var result = mockMvc.perform(
            patch("/v1/users/1/reset-password")
                .content(objectMapper
                    .writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}"));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(domain.getId()))
            .andExpect(jsonPath("$.name").value(domain.getName()))
            .andExpect(jsonPath("$.role").value(domain.getRole()))
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.snsId").value(domain.getSnsId()))
            .andExpect(jsonPath("$.provider").value(domain.getProvider()))
            .andExpect(jsonPath("$.email").value(domain.getEmail()))
            .andDo(MockMvcRestDocumentation.document(
                "reset-password",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰")),
                requestFields(
                    fieldWithPath("password")
                        .description("password")),
                responseFields(
                    fieldWithPath("id").description(
                        "user identifier"),
                    fieldWithPath("name").description(
                        "name"),
                    fieldWithPath("role").description(
                        "role"),
                    fieldWithPath("updatedAt")
                        .description("updatedAt"),
                    fieldWithPath("createdAt")
                        .description("createdAt"),
                    fieldWithPath("snsId").description(
                        "snsId"),
                    fieldWithPath("provider")
                        .description("provider"),
                    fieldWithPath("email").description(
                        "email"))));
    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    public void deleteUserTest() throws Exception {
        var result = mockMvc.perform(
            delete("/v1/users/1")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}"));

        result.andExpect(status().isNoContent())
            .andDo(MockMvcRestDocumentation.document(
                "delete-user",
                getDocumentRequest(),
                getDocumentResponse(),
                requestHeaders(
                    headerWithName("Authorization")
                        .description("인증 토큰"))));
    }
}
