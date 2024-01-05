package com.meteormin.friday.common.request.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This annotation is for form data parameter.
 *
 * @author seongminyoo
 * @see com.meteormin.friday.common.request.FormDataParameterResolver
 * @since 2023/10/15
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
public @interface FormData {
}
