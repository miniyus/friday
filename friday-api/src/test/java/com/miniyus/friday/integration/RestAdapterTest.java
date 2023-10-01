package com.miniyus.friday.integration;

import com.epages.restdocs.apispec.ResourceSnippetParameters;
import com.epages.restdocs.apispec.ResourceSnippetParametersBuilder;
import com.epages.restdocs.apispec.Schema;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.javafaker.Faker;
import com.miniyus.friday.infrastructure.jwt.JwtProvider;
import com.miniyus.friday.infrastructure.jwt.JwtService;
import com.miniyus.friday.infrastructure.jwt.config.JwtConfiguration;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.infrastructure.security.PrincipalUserDetailsService;
import com.miniyus.friday.infrastructure.security.PrincipalUserService;
import com.miniyus.friday.infrastructure.security.config.SecurityConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Import;
import org.springframework.data.jpa.mapping.JpaMetamodelMappingContext;
import org.springframework.restdocs.RestDocumentationContextProvider;
import org.springframework.restdocs.RestDocumentationExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.documentationConfiguration;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@AutoConfigureRestDocs
@ActiveProfiles("test")
@Import(SecurityConfiguration.class)
@MockBean(JpaMetamodelMappingContext.class)
@ExtendWith({RestDocumentationExtension.class})
public abstract class RestAdapterTest {
    @Autowired
    protected WebApplicationContext ctx;

    protected MockMvc mockMvc;

    @MockBean
    protected PrincipalUserDetailsService userDetailsService;

    @MockBean
    protected PrincipalUserService userService;

    @MockBean
    protected JwtService jwtService;

    @MockBean
    protected JwtConfiguration jwtConfiguration;

    @MockBean
    protected UserEntityRepository userEntityRepository;

    @MockBean
    protected MessageSource messageSource;

    protected final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    protected final ObjectMapper objectMapper = new ObjectMapper();

    protected final Faker faker = new Faker();

    protected JwtProvider jwtProvider;

    protected String accessToken;

    @BeforeEach
    public void setUp(final RestDocumentationContextProvider restDocumentation) {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(ctx)
            .apply(documentationConfiguration(restDocumentation))
            .apply(springSecurity())
            .addFilter(new CharacterEncodingFilter("UTF-8", true))
            .alwaysDo(print())
            .build();

        this.jwtProvider = new JwtProvider(
            "jwt-secret",
            3600L,
            86400L,
            "Authorization",
            "RefreshToken"
        );

        this.accessToken = jwtProvider.createAccessToken(faker.internet().safeEmailAddress());
    }

    protected ResourceSnippetParametersBuilder getResourceBuilder(
        String tag,
        String summary,
        String description
    ) {
        return ResourceSnippetParameters.builder()
            .tag(tag)
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
}
