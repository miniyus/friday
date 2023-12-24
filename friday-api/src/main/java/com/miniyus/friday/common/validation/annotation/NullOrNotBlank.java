package com.miniyus.friday.common.validation.annotation;

import com.precisionbio.cuttysark.common.validation.NullOrNotBlankValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Allowed NULL but, not allowed blank
 */
@Target({ElementType.FIELD})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = NullOrNotBlankValidator.class)
public @interface NullOrNotBlank {
    /**
     * Validation fail message
     *
     * @return the message
     */
    String message() default "{validation.nullOrNotBlank}";

    /**
     * For the grouping
     *
     * @return the groups
     */
    Class<?>[] groups() default {};

    /**
     * payloads
     *
     * @return the payloads
     */
    Class<? extends Payload>[] payload() default {};
}
