package com.miniyus.friday.auth;

import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.miniyus.friday.api.auth.resource.AuthUserResource;
import com.miniyus.friday.auth.domain.Token;
import com.miniyus.friday.hexagonal.adapter.RestAdapterTest;
import com.miniyus.friday.infrastructure.jwt.IssueToken;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import com.miniyus.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import com.miniyus.friday.infrastructure.security.auth.userinfo.PasswordUserInfo;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultHandler;

import static com.epages.restdocs.apispec.ResourceDocumentation.headerWithName;
import static com.miniyus.friday.document.ApiDocumentUtils.getMockMvcDocumentResultHandler;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.post;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public abstract class AuthDocument extends RestAdapterTest {
    protected static final String tag = "auth";
    protected static final String summary = "auth api";
    protected static final String description = "auth api";

    protected ResourceSnippetParametersBuilder getAuthResourceBuilder() {
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

    protected ResultHandler signupDocument(
        String requestSchema,
        String responseSchema
    ) {
        var parameter =
            getResourceBuilder(requestSchema, responseSchema)
                .requestFields(
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("password").description("password")
                )
                .responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("role").description("role")
                ).build();

        return getMockMvcDocumentResultHandler("auth signup", parameter);
    }

    public void signup(PasswordUserInfo request, AuthUserResource response)
        throws Exception {
        var result = mockMvc.perform(
            post("/v1/auth/signup")
                .with(csrf().asHeader())
                .content(
                    objectMapper.writeValueAsString(request)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(
                signupDocument(
                    request.getClass().getName(),
                    response.getClass().getName()
                )
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(response.id()))
            .andExpect(jsonPath("email").value(response.email()))
            .andExpect(jsonPath("name").value(response.name()))
            .andExpect(jsonPath("role").value(response.role()));
    }

    protected ResultHandler signinDocument(
        String requestSchema,
        String responseSchema
    ) {
        var parameter =
            getResourceBuilder(requestSchema, responseSchema)
                .requestFields(
                    fieldWithPath("email").description("email"),
                    fieldWithPath("password").description("password"),
                    fieldWithPath("secret").description("secret")
                )
                .responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("tokens").description("tokens"),
                    fieldWithPath("tokens.tokenType").description("tokenType"),
                    fieldWithPath("tokens.accessToken").description("accessToken"),
                    fieldWithPath("tokens.expiresIn").description("expiresIn"),
                    fieldWithPath("tokens.refreshToken").description("refreshToken")
                ).build();

        return getMockMvcDocumentResultHandler("auth signin", parameter);
    }

    public void signin(PasswordAuthentication request, PasswordTokenResponse response)
        throws Exception {
        var result = mockMvc.perform(
            post("/v1/auth/signin")
                .with(csrf().asHeader())
                .content(
                    objectMapper.writeValueAsString(request)
                )
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(
                signinDocument(
                    request.getClass().getName(),
                    response.getClass().getName()
                )
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("id").value(response.id()))
            .andExpect(jsonPath("email").value(response.email()))
            .andExpect(jsonPath("name").value(response.name()))
            .andExpect(jsonPath("tokens").isNotEmpty())
            .andExpect(jsonPath("tokens.accessToken").value(response.tokens().accessToken()))
            .andExpect(jsonPath("tokens.expiresIn").value(response.tokens().expiresIn()))
            .andExpect(jsonPath("tokens.refreshToken").value(response.tokens().refreshToken()));
    }

    protected ResultHandler refreshDocument(
        String responseSchema
    ) {
        var parameter =
            getResourceBuilder(
                "refreshToken",
                responseSchema
            )
                .requestHeaders(
                    headerWithName("RefreshToken").description("refresh-token")
                )
                .responseFields(
                    fieldWithPath("tokenType").description("tokenType"),
                    fieldWithPath("accessToken").description("accessToken"),
                    fieldWithPath("expiresIn").description("expiresIn"),
                    fieldWithPath("refreshToken").description("refreshToken")
                )
                .responseSchema(Schema.schema(IssueToken.class.getName()))
                .build();

        return getMockMvcDocumentResultHandler("auth refresh", parameter);
    }

    public void refresh(String request, Token response) throws Exception {
        var result = mockMvc.perform(
            post("/v1/auth/refresh-token")
                .with(csrf().asHeader())
                .header("RefreshToken", request)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
        );

        result.andDo(
                refreshDocument(response.getClass().getName())
            ).andExpect(status().isCreated())
            .andExpect(jsonPath("tokenType").value(response.tokenType()))
            .andExpect(jsonPath("accessToken").value(response.accessToken()))
            .andExpect(jsonPath("expiresIn").value(response.expiresIn()))
            .andExpect(jsonPath("refreshToken").value(response.refreshToken()));
    }

    protected ResultHandler userInfoDocument(
        String responseSchema
    ) {
        var parameter =
            getAuthResourceBuilder()
                .privateResource(true)
                .responseFields(
                    fieldWithPath("id").description("id"),
                    fieldWithPath("email").description("email"),
                    fieldWithPath("name").description("name"),
                    fieldWithPath("snsId").description("snsId"),
                    fieldWithPath("provider").description("provider"),
                    fieldWithPath("role").description("role")
                )
                .responseSchema(Schema.schema(responseSchema))
                .build();

        return getMockMvcDocumentResultHandler("auth user info", parameter);
    }

    public void userInfo(AuthUserResource response) throws Exception {
        var result = mockMvc.perform(
            get("/v1/auth/me")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        result.andDo(
                userInfoDocument(response.getClass().getName())
            ).andExpect(status().isOk())
            .andExpect(jsonPath("id").value(response.id()))
            .andExpect(jsonPath("email").value(response.email()))
            .andExpect(jsonPath("name").value(response.name()))
            .andExpect(jsonPath("snsId").value(response.snsId()))
            .andExpect(jsonPath("role").value(response.role()))
            .andExpect(jsonPath("provider").value(response.provider()));
    }

    protected ResultHandler logoutDocument() {
        return getMockMvcDocumentResultHandler("auth logout",
            getAuthResourceBuilder()
                .privateResource(true)
                .build()
        );
    }

    public void logout() throws Exception {
        var result = mockMvc.perform(
            post("/v1/auth/logout")
                .with(csrf())
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
        );

        result.andDo(
            logoutDocument()
        ).andExpect(status().isNoContent());
    }
}
