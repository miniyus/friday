package com.miniyus.friday.infrastructure.security;

import com.miniyus.friday.common.error.RestErrorCode;
import com.miniyus.friday.common.error.RestErrorException;
import com.miniyus.friday.infrastructure.persistence.entities.UserEntity;
import com.miniyus.friday.infrastructure.persistence.repositories.UserEntityRepository;
import com.miniyus.friday.infrastructure.security.annotation.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.core.MethodParameter;
import org.springframework.lang.NonNull;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

/**
 * 인증 유저 정보를 controller 메서드에 주입
 *
 * @author seongminyoo
 * @since 2023/10/23
 */
@Component
@RequiredArgsConstructor
public class AuthUserMethodResolver implements HandlerMethodArgumentResolver {
    /**
     * user entity repository
     */
    private final UserEntityRepository repository;

    /**
     * Determines if the specified method parameter is supported.
     *
     * @param parameter the method parameter to check
     * @return true if the parameter is supported, false otherwise
     */
    @Override
    public boolean supportsParameter(@NonNull MethodParameter parameter) {
        final boolean isAuthUser = parameter.getParameterAnnotation(AuthUser.class) != null;
        final boolean isAuthUserClass =
            parameter.getParameterType().equals(PrincipalUserInfo.class);
        AuthUser annotation = parameter.getParameterAnnotation(AuthUser.class);
        if (annotation == null) {
            return false;
        }

        if (!isAuthUserClass && annotation.entity()) {
            return isAuthUser && parameter.getParameterType().equals(UserEntity.class);
        }

        return isAuthUser && isAuthUserClass;
    }

    /**
     * Resolves the argument for a Java function.
     *
     * @param parameter     the method parameter being resolved
     * @param mavContainer  the ModelAndViewContainer for the current request
     * @param webRequest    the NativeWebRequest for the current request
     * @param binderFactory the WebDataBinderFactory for the current request
     * @return the resolved argument
     */
    @Override
    public Object resolveArgument(
        @NonNull MethodParameter parameter,
        ModelAndViewContainer mavContainer,
        @NonNull NativeWebRequest webRequest,
        WebDataBinderFactory binderFactory) {
        var annotation = parameter.getParameterAnnotation(AuthUser.class);
        var auth = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userInfo = (PrincipalUserInfo) auth;
        assert annotation != null;
        if (annotation.entity()) {
            return repository.findById(userInfo.getId())
                .orElseThrow(() -> new RestErrorException(RestErrorCode.ACCESS_DENIED));
        }
        return userInfo;
    }
}
