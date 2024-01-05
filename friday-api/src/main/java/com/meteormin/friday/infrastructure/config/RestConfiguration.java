package com.meteormin.friday.infrastructure.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.meteormin.friday.common.fake.FakeInjector;
import com.meteormin.friday.common.request.FormDataParameterResolver;
import com.meteormin.friday.common.request.QueryParameterResolver;
import com.meteormin.friday.common.util.FakeUtil;
import com.meteormin.friday.common.util.RequestFilter;
import com.meteormin.friday.infrastructure.security.AuthUserMethodResolver;
import lombok.RequiredArgsConstructor;
import net.datafaker.Faker;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * RestConfiguration custom bean and configurations
 *
 * @author seongminyoo
 * @since 2023/09/14
 */
@Configuration
@RequiredArgsConstructor
@EnableTransactionManagement(proxyTargetClass = true)
public class RestConfiguration implements WebMvcConfigurer {
    /**
     * api endpoint prefix
     */
    public static final String PREFIX = "/v1";

    /**
     * query parameter resolver
     */
    private final QueryParameterResolver queryParameterResolver;

    /**
     * form data parameter resolver
     */
    private final FormDataParameterResolver formDataParameterResolver;

    /**
     * auth user resolver
     */
    private final AuthUserMethodResolver authUserMethodResolver;

    /**
     * application name
     */
    @Value("${info.app.name}")
    private String name;

    /**
     * application version
     */
    @Value("${info.app.version}")
    private String version;

    /**
     * app secret
     */
    @Value("${app.secret}")
    private String secret;

    /**
     * active spring profile
     */
    @Value("${spring.profiles.active}")
    private String profile;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(queryParameterResolver);
        argumentResolvers.add(formDataParameterResolver);
        argumentResolvers.add(authUserMethodResolver);
    }

    @Bean
    public FakeInjector fakeInjector() {
        var objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());

        var injector = new FakeInjector(
            new Faker(),
            objectMapper
        );

        FakeUtil.setInjector(injector);

        return injector;
    }

    @Bean
    public String appName() {
        return name;
    }

    @Bean
    public String appVersion() {
        return version;
    }

    @Bean
    public String appSecret() {
        return secret;
    }

    @Bean
    public String appProfile() {
        return profile;
    }

    @Bean
    @SuppressWarnings({"rawtypes", "unchecked"})
    public FilterRegistrationBean reReadableRequestFilter() {
        FilterRegistrationBean filterRegistrationBean =
            new FilterRegistrationBean(new RequestFilter());
        filterRegistrationBean.setUrlPatterns(List.of("/*"));
        return filterRegistrationBean;
    }
}
