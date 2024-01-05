package com.meteormin.friday.common.validation.annotation;

import com.meteormin.friday.common.validation.EnumValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Enum validation
 *
 * @author seongminyoo
 * @see EnumValidator
 * @since 2023/09/14
 */
@Constraint(validatedBy = {EnumValidator.class})
@Target({ElementType.METHOD, ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Enum {
    /**
     * Validation fail message
     *
     * @return the message
     */
    String message() default "Invalid value. This is not permitted.";

    /**
     * For the grouping
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * The Payloads
     *
     * @return payloads
     */
    Class<? extends Payload>[] payload() default {};

    /**
     * Valid enum class
     *
     * @return the enum
     */
    Class<? extends java.lang.Enum<?>> enumClass();

    /**
     * Ignore case.
     *
     * @return default false.
     */
    boolean ignoreCase() default false;

    /**
     * nullable.
     *
     * @return default false.
     */
    boolean nullable() default false;
}
