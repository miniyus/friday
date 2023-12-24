package com.miniyus.friday.common.request;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniyus.friday.common.request.annotation.FormData;
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
public class FormDataParameterResolver implements HandlerMethodArgumentResolver {
    private final ObjectMapper objectMapper;
    private final Validator validator;

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterAnnotation(FormData.class) != null;
    }

    @Override
    public Object resolveArgument(
        final MethodParameter parameter,
        final ModelAndViewContainer mavContainer,
        final NativeWebRequest webRequest,
        final WebDataBinderFactory binderFactory
    ) throws MethodArgumentNotValidException {
        Map<String, String[]> params = webRequest.getParameterMap();
        var resolveParameter = objectMapper.convertValue(formDataToMap(params), parameter.getParameterType());
        var dataBinder = new DataBinder(resolveParameter);
        dataBinder.setValidator(validator);
        dataBinder.validate();
        if (dataBinder.getBindingResult().hasErrors()) {
            throw new MethodArgumentNotValidException(parameter, dataBinder.getBindingResult());
        }
        return resolveParameter;
    }

    private Map<String, Object> formDataToMap(Map<String, String[]> formData) {
        var map = new HashMap<String, Object>();
        for (Map.Entry<String, String[]> entry : formData.entrySet()) {
            var value = entry.getValue()[0];
            map.put(entry.getKey(), value);
        }

        return map;
    }
}
