package com.meteormin.friday.common.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

/**
 * Conditional validation
 */
@Target({ElementType.ANNOTATION_TYPE})
public @interface Condition {
    /**
     * The data type class
     * @return the data type
     */
    Class<?> type() default String.class;

    /**
     * Conditional value
     * @return the value
     */
    String value() default "";
}
