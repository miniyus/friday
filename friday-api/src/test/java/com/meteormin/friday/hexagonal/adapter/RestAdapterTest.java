package com.meteormin.friday.hexagonal.adapter;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meteormin.friday.common.fake.FakeInjector;
import com.meteormin.friday.infrastructure.config.JwtConfiguration;
import com.meteormin.friday.infrastructure.config.SecurityConfiguration;
import com.meteormin.friday.infrastructure.jwt.JwtProvider;
import com.meteormin.friday.infrastructure.jwt.JwtService;
import com.meteormin.friday.infrastructure.persistence.repositories.LoginHistoryEntityRepository;
import com.meteormin.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.meteormin.friday.infrastructure.security.PrincipalUserDetailsService;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.lang.NonNull;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMultipartHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.RequestPostProcessor;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.multipart;

@AutoConfigureMockMvc
@AutoConfigureRestDocs
@ActiveProfiles("test")
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class})
@Import({SecurityConfiguration.class})
public abstract class RestAdapterTest {
    @Autowired
    protected WebApplicationContext ctx;

    @Autowired
    protected MockMvc mockMvc;

    @MockBean
    protected PrincipalUserDetailsService userDetailsService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected JwtConfiguration jwtConfiguration;

    @MockBean
    protected UserEntityRepository userEntityRepository;

    @MockBean
    protected LoginHistoryEntityRepository loginHistoryEntityRepository;

    @MockBean
    protected MessageSource messageSource;

    @MockBean
    protected OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService;

    protected final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected final Faker faker = new Faker();

    protected FakeInjector fakeInjector;

    protected JwtProvider jwtProvider;

    protected String accessToken;

    protected String tag;

    @BeforeEach
    public void setUp() {
        jwtProvider = new JwtProvider(
            "jwt-secret",
            3600L,
            86400L,
            "Authorization",
            "RefreshToken"
        );
        accessToken = jwtProvider.createAccessToken(faker.internet().safeEmailAddress());
        objectMapper.registerModule(new JavaTimeModule());
        fakeInjector = new FakeInjector(faker, objectMapper);
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder() {
        return ResourceSnippetParameters.builder();
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(String tag) {
        return getResourceBuilder().tag(tag);
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String tag,
        String summary,
        String description
    ) {
        return getResourceBuilder(tag)
            .summary(summary)
            .description(description);
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String tag,
        String summary,
        String description,
        String requestSchema,
        String responseSchema
    ) {
        return getResourceBuilder(
            tag,
            summary,
            description
        )
            .requestSchema(Schema.schema(requestSchema))
            .responseSchema(Schema.schema(responseSchema));
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String summary,
        String description,
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

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String summary,
        String description
    ) {
        return getResourceBuilder(
            tag,
            summary,
            description
        );
    }

    protected MockMultipartHttpServletRequestBuilder customMultipart(HttpMethod method, String url,
        Object... urlVariables) {
        var builder = multipart(url, urlVariables);
        builder.accept(MediaType.APPLICATION_JSON)
            .contentType(MediaType.MULTIPART_FORM_DATA)
            .with(new RequestPostProcessor() {
                @NonNull
                @Override
                public MockHttpServletRequest postProcessRequest(@NonNull MockHttpServletRequest request) {
                    request.setMethod(method.name());
                    return request;
                }
            });

        return builder;
    }

}
