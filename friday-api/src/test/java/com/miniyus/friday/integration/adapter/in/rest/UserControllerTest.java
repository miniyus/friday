package com.miniyus.friday.integration.adapter.in.rest;

import static com.epages.restdocs.apispec.MockMvcRestDocumentationWrapper.*;
import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.miniyus.friday.adapter.in.rest.UserController;
import com.miniyus.friday.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.adapter.in.rest.request.ResetPasswordRequest;
import com.miniyus.friday.adapter.in.rest.request.UpdateUserRequest;
import com.miniyus.friday.adapter.in.rest.resource.UserResources.UserResource;
import com.miniyus.friday.application.port.in.query.RetrieveUserQuery;
import com.miniyus.friday.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.application.port.in.usecase.DeleteUserUsecase;
import com.miniyus.friday.application.port.in.usecase.UpdateUserUsecase;
import com.miniyus.friday.common.UserRole;
import com.miniyus.friday.domain.users.User;
import com.miniyus.friday.integration.RestAdapterTest;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;

import static com.epages.restdocs.apispec.ResourceDocumentation.resource;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentRequest;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.restdocs.headers.HeaderDocumentation.headerWithName;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * user controller test
 *
 * @author miniyus
 * @date 2023/09/07
 */
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest extends RestAdapterTest {
    @MockBean
    private CreateUserUsecase createUserUsecase;

    @MockBean
    private RetrieveUserQuery retrieveUserQuery;

    @MockBean
    private UpdateUserUsecase updateUserUsecase;

    @MockBean
    private DeleteUserUsecase deleteUserUsecase;

    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

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

        when(createUserUsecase.createUser(any())).thenReturn(domain);

        UserResource response = UserResource.fromDomain(domain);

        var request = CreateUserRequest.builder().email("miniyu97@gmail.com")
            .name("tester").password("password@1234")
            .role(UserRole.USER.getValue()).build();

        ResultActions result = this.mockMvc.perform(
            post("/v1/users")
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(response.id()))
            .andExpect(jsonPath("$.email").value(response.email()))
            .andExpect(jsonPath("$.name").value(response.name()))
            .andExpect(jsonPath("$.role").value(response.role()))
            .andExpect(jsonPath("$.snsId").value(response.snsId()))
            .andExpect(jsonPath("$.provider").value(response.provider()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andDo(document(
                "create-user",
                getDocumentRequest(),
                getDocumentResponse(),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("create user")
                        .requestHeaders(
                            headerWithName("Authorization")
                                .description("인증 토큰")
                        )
                        .requestFields(
                            fieldWithPath("email")
                                .description("이메일"),
                            fieldWithPath("name")
                                .description("이름"),
                            fieldWithPath("password")
                                .description("비밀번호"),
                            fieldWithPath("role").description(
                                "역할")
                        ).responseFields(
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
                                .description("updatedAt")
                        ).build()
                )
            ));
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

        when(retrieveUserQuery.findById(1L)).thenReturn(domain);

        var result = mockMvc.perform(
            get("/v1/users/{id}", 1)
                .with(csrf().asHeader())
                //                .header("Authorization", "Bearer {access-token}")
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(domain.getId()))
            .andExpect(jsonPath("$.name").value(domain.getName()))
            .andExpect(jsonPath("$.email").value(domain.getEmail()))
            .andExpect(jsonPath("$.role").value(domain.getRole()))
            .andExpect(jsonPath("$.snsId").value(domain.getSnsId()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty())
            .andDo(document(
                "retrieve-user",
                getDocumentRequest(),
                getDocumentResponse(),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("retrieve user")
                        .pathParameters(
                            parameterWithName("id")
                                .description("user identifier")
                        )
                        .responseFields(
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
                                .description("updatedAt")
                        ).build()
                )
            ));
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

        domain.patch("updateName", null);

        when(updateUserUsecase.patchUser(any())).thenReturn(
            domain);

        var request = UpdateUserRequest.builder()
            .name("updateName")
            .role("USER")
            .build();

        var result = mockMvc.perform(
            patch("/v1/users/{id}", 1)
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
            .andDo(document(
                "update-user",
                getDocumentRequest(),
                getDocumentResponse(),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("update user")
                        .requestHeaders(
                            headerWithName("Authorization")
                                .description("인증 토큰")
                        )
                        .pathParameters(
                            parameterWithName("id")
                                .description("user identifier")
                        )
                        .requestFields(
                            fieldWithPath("name").description(
                                "name"),
                            fieldWithPath("role").description(
                                "role")
                        )
                        .responseFields(
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
                                "email")
                        ).build()
                )));
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

        when(updateUserUsecase.resetPassword(any())).thenReturn(true);

        var request = new ResetPasswordRequest("resetPassword");

        var result = mockMvc.perform(
            patch("/v1/users/{id}/reset-password", 1)
                .content(objectMapper
                    .writeValueAsString(request))
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}"));

        result.andExpect(status().isOk())
            .andExpect(jsonPath("$.resetPassword").value(true))
            .andDo(document(
                "reset-password",
                getDocumentRequest(),
                getDocumentResponse(),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("reset password")
                        .requestHeaders(
                            headerWithName("Authorization")
                                .description("인증 토큰")
                        )
                        .pathParameters(
                            parameterWithName("id")
                                .description("user identifier")
                        )
                        .requestFields(
                            fieldWithPath("password")
                                .description("password")
                        )
                        .responseFields(
                            fieldWithPath("resetPassword").description(
                                "user identifier")
                        ).build()
                )
            ));
    }

    @Test
    @WithMockCustomUser(username = "tester@gmail.com", role = UserRole.USER)
    public void deleteUserTest() throws Exception {
        var result = mockMvc.perform(
            delete("/v1/users/{id}", 1)
                .accept(MediaType.APPLICATION_JSON)
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}"));

        result.andExpect(status().isNoContent())
            .andDo(document(
                "delete-user",
                getDocumentRequest(),
                getDocumentResponse(),
                resource(
                    ResourceSnippetParameters.builder()
                        .description("delete user")
                        .requestHeaders(
                            headerWithName("Authorization")
                                .description("인증 토큰")
                        )
                        .pathParameters(
                            parameterWithName("id")
                                .description("user identifier")
                        ).build()
                )
            ));
    }
}
