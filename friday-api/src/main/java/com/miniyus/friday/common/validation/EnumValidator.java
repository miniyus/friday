package com.miniyus.friday.common.validation;

import com.precisionbio.cuttysark.common.util.EnumUtil;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<Enum, String> {

    private Enum annotation;

    @Override
    public void initialize(Enum constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (this.annotation.nullable() && value == null) {
            return true;
        } else if (value == null) {
            return false;
        }

        try {
            var enumValue = EnumUtil.of(annotation.enumClass(), value, annotation.ignoreCase());
            return enumValue != null;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

}
