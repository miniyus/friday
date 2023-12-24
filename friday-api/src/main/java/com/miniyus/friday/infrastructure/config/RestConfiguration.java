package com.miniyus.friday.infrastructure.config;

import com.miniyus.friday.common.request.QueryParameterResolver;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class RestConfiguration implements WebMvcConfigurer {
    private final QueryParameterResolver queryParameterResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(queryParameterResolver);
    }
}
