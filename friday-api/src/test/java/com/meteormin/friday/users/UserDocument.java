package com.meteormin.friday.users;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.meteormin.friday.api.users.request.CreateUserRequest;
import com.meteormin.friday.api.users.request.ResetPasswordRequest;
import com.meteormin.friday.api.users.request.UpdateUserRequest;
import com.meteormin.friday.api.users.resource.ResetPasswordResource;
import com.meteormin.friday.api.users.resource.UserResources.UserResource;
import com.meteormin.friday.hexagonal.adapter.RestAdapterTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;

import static com.epages.restdocs.apispec.ResourceDocumentation.parameterWithName;
import static com.meteormin.friday.document.ApiDocumentUtils.getMockMvcDocumentResultHandler;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class UserDocument extends RestAdapterTest {
    protected String tag = "users";
    protected String summary = "user api";
    protected String description = "user api";

    protected ResourceSnippetParametersBuilder getUserResourceBuilder() {
        return getResourceBuilder(
            tag,
            summary,
            description
        );
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String requestSchema,
        String responseSchema
    ) {
        return getResourceBuilder(
            tag,
            summary,
            description,
            requestSchema,
            responseSchema
        );
    }

    protected ResultHandler createUserDocument(
        String requestSchema,
        String responseSchema
    ) {
        var parameter =
            getResourceBuilder(requestSchema, responseSchema)
                .privateResource(true)
                .requestFields(
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("password").description("password"),
                    fieldWithPath("role").description("role")
                )
                .responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("createdAt").description("createdAt"),
                    fieldWithPath("updatedAt").description("updatedAt")
                )
                .build();
        return getMockMvcDocumentResultHandler("create user", parameter);
    }

    protected void createUser(CreateUserRequest request, UserResource response) throws Exception {
        var result = mockMvc.perform(
            post("/v1/users")
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        result.andDo(
                createUserDocument(request.getClass().getName(), response.getClass().getName()))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(response.id()))
            .andExpect(jsonPath("$.email").value(response.email()))
            .andExpect(jsonPath("$.name").value(response.name()))
            .andExpect(jsonPath("$.role").value(response.role().value()))
            .andExpect(jsonPath("$.snsId").value(response.snsId()))
            .andExpect(jsonPath("$.provider").value(response.provider()))
            .andExpect(jsonPath("$.createdAt").isNotEmpty())
            .andExpect(jsonPath("$.updatedAt").isNotEmpty());
    }

    protected ResultHandler retrieveUserDocument(String responseSchema) {
        var parameter =
            getUserResourceBuilder()
                .privateResource(true)
                .pathParameters(
                    parameterWithName("id").description("user identifier")
                )
                .responseFields(
                    fieldWithPath("id").description("user identifier"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("createdAt").description("createdAt"),
                    fieldWithPath("updatedAt").description("updatedAt")
                )
                .responseSchema(Schema.schema(responseSchema)).build();

        return getMockMvcDocumentResultHandler("retrieve user", parameter);
    }

    protected void retrieveUser(Long id, UserResource response) throws Exception {
        var result = mockMvc.perform(
            get("/v1/users/{id}", id)
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(retrieveUserDocument(response.getClass().getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(response.id()))
            .andExpect(jsonPath("email").value(response.email()))
            .andExpect(jsonPath("name").value(response.name()))
            .andExpect(jsonPath("role").value(response.role().value()))
            .andExpect(jsonPath("snsId").value(response.snsId()))
            .andExpect(jsonPath("provider").value(response.provider()))
            .andExpect(jsonPath("createdAt").isNotEmpty())
            .andExpect(jsonPath("updatedAt").isNotEmpty());
    }

    protected ResultHandler updateUserDocument(String requestSchema, String responseSchema) {
        var parameter =
            getResourceBuilder(requestSchema, responseSchema)
                .privateResource(true)
                .pathParameters(
                    parameterWithName("id").description("user identifier")
                )
                .requestFields(
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role")
                )
                .responseFields(
                    fieldWithPath("id").description("user identifier"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("role").description("role"),
                    fieldWithPath("updatedAt").description("updatedAt"),
                    fieldWithPath("createdAt").description("createdAt"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("email").description("email")
                ).build();

        return getMockMvcDocumentResultHandler("update user", parameter);
    }

    protected void updateUser(Long id, UpdateUserRequest request, UserResource response)
        throws Exception {
        var result = mockMvc.perform(
            patch("/v1/users/{id}", id)
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content("{\"name\":\"" + request.name().get() + "\",\"role\":\"" + request.role()
                    .get() + "\"}")
        );

        result.andDo(
                updateUserDocument(request.getClass().getName(), response.getClass().getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id").value(response.id()))
            .andExpect(jsonPath("name").value(response.name()))
            .andExpect(jsonPath("role").value(response.role().value()))
            .andExpect(jsonPath("snsId").value(response.snsId()))
            .andExpect(jsonPath("provider").value(response.provider()))
            .andExpect(jsonPath("email").value(response.email()))
            .andExpect(jsonPath("updatedAt").isNotEmpty())
            .andExpect(jsonPath("createdAt").isNotEmpty());
    }

    protected ResultHandler resetPasswordDocument(String requestSchema, String responseSchema) {
        var parameter =
            getResourceBuilder(requestSchema, responseSchema)
                .privateResource(true)
                .pathParameters(
                    parameterWithName("id").description("user identifier")
                )
                .requestFields(
                    fieldWithPath("password").description("password")
                )
                .responseFields(
                    fieldWithPath("resetPassword").description("reset password")
                ).build();

        return getMockMvcDocumentResultHandler("reset password", parameter);
    }

    protected void resetPassword(
        Long id,
        ResetPasswordRequest request,
        ResetPasswordResource response
    ) throws Exception {
        var result = mockMvc.perform(
            patch("/v1/users/{id}/reset-password", id)
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
        );

        result.andDo(
                resetPasswordDocument(request.getClass().getName(), response.getClass().getName()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("resetPassword").value(response.resetPassword()));
    }

    protected ResultHandler deleteUserDocument() {
        var parameter =
            getUserResourceBuilder()
                .privateResource(true)
                .pathParameters(
                    parameterWithName("id").description("user identifier")
                ).build();

        return getMockMvcDocumentResultHandler("delete user", parameter);
    }

    protected void deleteUser(Long id) throws Exception {
        var result = mockMvc.perform(
            delete("/v1/users/{id}", id)
                .with(csrf().asHeader())
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(deleteUserDocument())
            .andExpect(status().isNoContent());
    }
}
