package com.miniyus.friday.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.infrastructure.security.auth.PasswordAuthentication;
import com.miniyus.friday.infrastructure.security.auth.filter.PasswordAuthenticationFilter;
import com.miniyus.friday.infrastructure.security.auth.response.PasswordTokenResponse;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.PathItem;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.context.AbstractSecurityWebApplicationInitializer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Configuration
@Profile("local || dev")
@RequiredArgsConstructor
public class OpenApiConfiguration {
    public static final String PREFIX = RestConfiguration.PREFIX;
    public static final String CONSOLE_PREFIX = RestConfiguration.CONSOLE_PREFIX;
    public static final String DMS_PREFIX = RestConfiguration.DMS_PREFIX;

    private final ApplicationContext applicationContext;

    private final ObjectMapper objectMapper;

    @Value("${app.name}")
    private String name;

    @Value("${app.version}")
    private String version;

    @Bean
    public OpenAPI openAPI() throws IOException {
        String tokenType = "Bearer";

        Info info = new Info()
            .version(version)
            .title(name)
            .description("Cuttysark API Documentation.");

        SecurityScheme securityScheme = new SecurityScheme()
            .name(tokenType)
            .type(SecurityScheme.Type.HTTP)
            .bearerFormat("JWT")
            .scheme("bearer");

        return new OpenAPI()
            .components(new Components())
            .info(info)
            .servers(servers())
            .schemaRequirement(tokenType, securityScheme)
            .addSecurityItem(
                new SecurityRequirement()
                    .addList("Bearer", new ArrayList<>()));
    }

    @Bean
    public GroupedOpenApi groupApp() {
        return GroupedOpenApi.builder()
            .group("api")
            .pathsToMatch(PREFIX + "/**")
            .pathsToExclude(CONSOLE_PREFIX + "/**", DMS_PREFIX + "/**")
            .build();
    }

    @Bean
    public OpenApiCustomizer springSecurityLoginEndpointCustomizer() {
        FilterChainProxy filterChainProxy = applicationContext.getBean(
            AbstractSecurityWebApplicationInitializer.DEFAULT_FILTER_NAME, FilterChainProxy.class);
        return openAPI -> {
            for (SecurityFilterChain filterChain : filterChainProxy.getFilterChains()) {
                Optional<PasswordAuthenticationFilter> optionalFilter =
                    filterChain.getFilters().stream()
                        .filter(PasswordAuthenticationFilter.class::isInstance)
                        .map(PasswordAuthenticationFilter.class::cast)
                        .findAny();
                if (optionalFilter.isPresent()) {
                    PasswordAuthenticationFilter passwordAuthenticationFilter =
                        optionalFilter.get();
                    Operation operation = new Operation();
                    RequestBody requestBody = new RequestBody().content(
                        new Content().addMediaType(
                            org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                            new MediaType().schema(
                                parseSchema(
                                    PasswordAuthentication.class,
                                    openAPI.getComponents().getSchemas())
                            )
                        ));

                    operation.requestBody(requestBody);
                    ApiResponses apiResponses = new ApiResponses();
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.CREATED.value()),
                        new ApiResponse().description(HttpStatus.CREATED.getReasonPhrase())
                            .content(new Content().addMediaType(
                                org.springframework.http.MediaType.APPLICATION_JSON_VALUE,
                                new MediaType().schema(parseSchema(
                                    PasswordTokenResponse.class,
                                    openAPI.getComponents().getSchemas()))
                            )));
                    apiResponses.addApiResponse(String.valueOf(HttpStatus.BAD_REQUEST.value()),
                        new ApiResponse().description(HttpStatus.BAD_REQUEST.getReasonPhrase()));
                    operation.responses(apiResponses);
                    operation.addTagsItem("Auth");
                    PathItem pathItem = new PathItem().post(operation);
                    openAPI.getPaths()
                        .addPathItem(passwordAuthenticationFilter.getDefaultLoginRequestUrl(),
                            pathItem);
                }
            }
        };
    }

    @SuppressWarnings("rawtypes")
    private Schema<?> parseSchema(Class<?> clazz, Map<String, Schema> schemaMap) {
        var schema = ModelConverters.getInstance().readAllAsResolvedSchema(clazz);
        if (schema.referencedSchemas == null) {
            return schema.schema;
        }

        for (String key : schema.referencedSchemas.keySet()) {
            schemaMap.computeIfAbsent(key, k -> schema.referencedSchemas.get(k));
        }

        return schema.schema;
    }

    private List<Server> servers() throws IOException {
        List<Server> servers = new ArrayList<>();
        servers.add(new Server().url("http://localhost:8888"));
        ClassPathResource resource = new ClassPathResource("static/openapi/servers.json");
        InputStream inputStream = resource.getInputStream();
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuilder content = new StringBuilder();

        String line;
        while ((line = reader.readLine()) != null) {
            content.append(line);
        }

        var jsonNode = objectMapper.readTree(content.toString());

        jsonNode.get("servers").forEach(node -> {
            var server = new Server();
            server.url(node.get("url").asText());
            server.description(node.get("description").asText());
            servers.add(server);
        });

        String serverUrl = System.getenv("SERVER_URL");
        if (serverUrl != null) {
            Server server = new Server().url(serverUrl);
            servers.add(server);
        }

        return servers;
    }
}
