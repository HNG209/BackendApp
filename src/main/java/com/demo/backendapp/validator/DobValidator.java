package com.demo.backendapp.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public class DobValidator implements ConstraintValidator<Dob, LocalDate> {

    private int min;

    @Override
    public void initialize(Dob constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
        min = constraintAnnotation.min();
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        if(Objects.isNull(value))
            return true;

        long year = ChronoUnit.YEARS.between(value, LocalDate.now());

        return year >= min;
    }
}
