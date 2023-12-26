package com.miniyus.friday.common.request;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.request.annotation.QueryParam;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.validation.DataBinder;
import org.springframework.validation.Validator;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class QueryParameterResolver implements HandlerMethodArgumentResolver {

    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(QueryParam.class) != null;
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) throws Exception {
        final HttpServletRequest request = (HttpServletRequest) webRequest.getNativeRequest();

        final Map<String, Object> map = qs2Map(request.getQueryString());

        var resolveParameter = objectMapper.convertValue(map, parameter.getParameterType());
        var dataBinder = new DataBinder(resolveParameter);
        dataBinder.setValidator(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            throw new MethodArgumentNotValidException(parameter, dataBinder.getBindingResult());
        }

        return resolveParameter;
    }

    private Map<String, Object> qs2Map(String queryString) {
        Map<String, Object> map = new HashMap<>();

        if (queryString == null || queryString.isBlank()) {
            return map;
        }

        String[] pairs = queryString.split("&");
        for (String pair : pairs) {
            String[] keyValue = pair.split("=");
            if (keyValue.length == 2) {
                String key = keyValue[0];
                String value = keyValue[1];

                // 배열로 처리할 경우
                if (value.contains(",")) {
                    String[] arrayValues = value.split(",");
                    map.put(key, arrayValues);
                } else {
                    map.put(key, value);
                }
            }
        }

        return map;
    }
}
