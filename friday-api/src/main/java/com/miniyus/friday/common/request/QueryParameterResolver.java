package com.miniyus.friday.common.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.request.annotation.QueryParam;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@RequiredArgsConstructor
@Component
public class QueryParameterResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(QueryParam.class) != null;
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory) throws Exception {

        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();
        final String json = qs2json(request.getQueryString());
        return objectMapper.readValue(json, parameter.getParameterType());
    }

    private String qs2json(String a) {

        if (a == null) {
            return "{}";
        }

        StringBuilder res = new StringBuilder("{\"");

        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) == '=') {
                res.append("\"" + ":" + "\"");
            } else if (a.charAt(i) == '&') {
                res.append("\"" + "," + "\"");
            } else {
                res.append(a.charAt(i));
            }
        }

        res.append("\"" + "}");

        return res.toString();
    }
}
