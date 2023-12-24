package com.miniyus.friday.common.validation;

import com.precisionbio.cuttysark.common.validation.annotation.NullOrNotBlank;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class NullOrNotBlankValidator implements ConstraintValidator<NullOrNotBlank, String> {
    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return value == null || !value.trim().isEmpty();
    }
}
