package com.miniyus.friday.common.request.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for query parameter.
 *
 * @author seongminyoo
 * @see com.miniyus.friday.common.request.QueryParameterResolver
 * @since 2023/09/14
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface QueryParam {
}
