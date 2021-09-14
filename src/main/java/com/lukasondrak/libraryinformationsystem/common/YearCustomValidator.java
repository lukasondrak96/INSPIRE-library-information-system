package com.lukasondrak.libraryinformationsystem.common;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class YearCustomValidator implements ConstraintValidator<YearCustomConstraint, Integer> {

    @Override
    public void initialize(YearCustomConstraint year) {
    }

    @Override
    public boolean isValid(Integer year,
                           ConstraintValidatorContext cxt) {
        return year != null && year > 0 && year <= LocalDate.now().getYear();
    }
}
