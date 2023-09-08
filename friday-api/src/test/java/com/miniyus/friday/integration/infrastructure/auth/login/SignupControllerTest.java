package com.miniyus.friday.integration.infrastructure.auth.login;

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
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.auth.login.SignupController;
import com.miniyus.friday.infrastructure.auth.login.userinfo.PasswordUserInfo;
import com.miniyus.friday.infrastructure.jpa.repositories.RefreshTokenRepository;
import com.miniyus.friday.infrastructure.jpa.repositories.UserRepository;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.jwt.config.AccessConfiguration;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;
import com.miniyus.friday.infrastructure.jwt.config.RefreshConfiguration;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentRequest;
import static com.miniyus.friday.restdoc.ApiDocumentUtils.getDocumentResponse;
import static org.springframework.restdocs.payload.PayloadDocumentation.fieldWithPath;
import static org.springframework.restdocs.payload.PayloadDocumentation.requestFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * [description]
 *
 * @author miniyus
 * @date 2023/09/08
 */
@WebMvcTest(controllers = SignupController.class)
@MockBean(JpaMetamodelMappingContext.class)
@AutoConfigureMockMvc
@ActiveProfiles("test")
@AutoConfigureRestDocs(
        uriScheme = "http",
        uriHost = "localhost",
        uriPort = 8080,
        outputDir = "build/generated-snippets")
public class SignupControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserDetailsService userDetailsService;

    @MockBean
    private JwtService jwtService;

    @MockBean
    private JwtConfiguration jwtConfiguration;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private RefreshTokenRepository refreshTokenRepository;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private JwtProvider jwtProvider;

    @BeforeEach
    public void setUp() {
        jwtConfiguration = new JwtConfiguration();
        jwtConfiguration.setSecret("jwt-secret");
        jwtConfiguration.setAccess(new AccessConfiguration(3600L, "Authorization"));
        jwtConfiguration.setRefresh(new RefreshConfiguration(86400L, "RefreshToken"));

        jwtProvider = new JwtProvider(
                jwtConfiguration.getSecret(),
                jwtConfiguration.getAccess().getExpiration(),
                jwtConfiguration.getRefresh().getExpiration(),
                jwtConfiguration.getAccess().getHeader(),
                jwtConfiguration.getRefresh().getHeader());

        jwtService = new JwtService(jwtProvider, userRepository, refreshTokenRepository);
    }

    @Test
    public void signupTest() throws Exception {
        PasswordUserInfo request = PasswordUserInfo
                .builder()
                .email("testser@gmail.com")
                .name("smyoo")
                .password("password@1234")
                .role("USER")
                .build();

        ResultActions result = this.mockMvc.perform(
                post("/v1/auth/signup")
                        .with(csrf().asHeader())
                        .content(objectMapper.writeValueAsString(request))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated())
                .andExpect(jsonPath("$.email").value(request.getEmail()))
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
                                fieldWithPath("email").description("email"),
                                fieldWithPath("name").description("name"),
                                fieldWithPath("role").description("role"),
                                fieldWithPath("snsId").description("snsId"),
                                fieldWithPath("provider").description("provider"),
                                fieldWithPath("createdAt").description("createdAt"),
                                fieldWithPath("updatedAt").description("updatedAt"))));
    }
}
