package com.miniyus.friday.integration.users;

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
import com.miniyus.friday.infrastructure.auth.UserRole;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import com.miniyus.friday.users.adapter.in.rest.UserController;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.response.CreateUserResponse;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.users.domain.User;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

/**
 * [description]
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
public class CreateUserUsecaseTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private CreateUserUsecase createUserUsecase;

    @Test
    @WithMockCustomUser(username = "testser@gmail.com", role = UserRole.USER)
    public void createUserTest() throws Exception {
        User domain = new User(1L, "miniyu97@gmail.com", "password@1234", "smyoo", "USER", null,
                null, LocalDateTime.now(), LocalDateTime.now(), null);

        when(createUserUsecase.createUser(any(CreateUserCommand.class))).thenReturn(domain);

        CreateUserResponse response = CreateUserResponse.fromDomain(domain);

        CreateUserRequest request = CreateUserRequest.builder().email("miniyu97@gmail.com")
                .name("smyoo").password("password@1234").role(UserRole.USER.getValue()).build();

        ResultActions result = this.mockMvc.perform(post("/v1/users")
                .with(csrf().asHeader())
                .header("Authorization", "Bearer {access-token}")
                .content(objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON).accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(response.getId()))
                .andExpect(jsonPath("$.email").value(response.getEmail()))
                .andExpect(jsonPath("$.name").value(response.getName()))
                .andExpect(jsonPath("$.role").value(response.getRole()))
                .andExpect(jsonPath("$.snsId").value(response.getSnsId()))
                .andExpect(jsonPath("$.provider").value(response.getProvider()))
                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                .andDo(MockMvcRestDocumentation.document(
                        "create-user",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        requestHeaders(
                                headerWithName("Authorization").description("인증 토큰")),
                        requestFields(
                                fieldWithPath("email").description("이메일"),
                                fieldWithPath("name").description("이름"),
                                fieldWithPath("password").description("비밀번호"),
                                fieldWithPath("role").description("역할")),
                        responseFields(
                                fieldWithPath("id").description("user identifier"),
                                fieldWithPath("email").description("email"),
                                fieldWithPath("name").description("name"),
                                fieldWithPath("role").description("role"),
                                fieldWithPath("snsId").description("snsId"),
                                fieldWithPath("provider").description("provider"),
                                fieldWithPath("createdAt").description("createdAt"),
                                fieldWithPath("updatedAt").description("updatedAt"))));
    }
}
