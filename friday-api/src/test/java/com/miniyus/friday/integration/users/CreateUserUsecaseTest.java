package com.miniyus.friday.integration.users;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.auth.UserRole;
import com.miniyus.friday.integration.annotation.WithMockCustomUser;
import com.miniyus.friday.users.adapter.in.rest.UserController;
import com.miniyus.friday.users.adapter.in.rest.request.CreateUserRequest;
import com.miniyus.friday.users.adapter.in.rest.response.CreateUserResponse;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserCommand;
import com.miniyus.friday.users.application.port.in.usecase.CreateUserUsecase;
import com.miniyus.friday.users.domain.User;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/07
 */
@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs(uriScheme = "http", uriHost = "localhost", uriPort = 8080, outputDir = "build/generated-snippets")
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
                User domain = new User(
                                1L,
                                "miniyu97@gmail.com",
                                "password@1234",
                                "smyoo",
                                "USER",
                                null,
                                null,
                                LocalDateTime.now(),
                                LocalDateTime.now(),
                                null);

                when(createUserUsecase.createUser(any(CreateUserCommand.class))).thenReturn(domain);

                CreateUserResponse response = CreateUserResponse.fromDomain(domain);

                CreateUserRequest request = CreateUserRequest.builder()
                                .email("miniyu97@gmail.com")
                                .name("smyoo")
                                .password("password@1234")
                                .role(UserRole.USER.getValue())
                                .build();

                ResultActions result = this.mockMvc.perform(
                                post("/v1/users").with(csrf().asHeader())
                                                .content(objectMapper.writeValueAsString(request))
                                                .contentType(MediaType.APPLICATION_JSON)
                                                .accept(MediaType.APPLICATION_JSON));

                result.andExpect(status().isCreated())
                                .andExpect(jsonPath("$.id").value(response.getId()))
                                .andExpect(jsonPath("$.email").value(response.getEmail()))
                                .andExpect(jsonPath("$.name").value(response.getName()))
                                .andExpect(jsonPath("$.role").value(response.getRole()))
                                .andExpect(jsonPath("$.snsId").value(response.getSnsId()))
                                .andExpect(jsonPath("$.provider").value(response.getProvider()))
                                .andExpect(jsonPath("$.createdAt").isNotEmpty())
                                .andExpect(jsonPath("$.updatedAt").isNotEmpty())
                                .andDo(MockMvcRestDocumentation.document("create-user"));
        }
}
