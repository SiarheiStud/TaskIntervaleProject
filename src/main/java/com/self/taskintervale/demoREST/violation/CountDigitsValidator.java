package com.self.taskintervale.demoREST.violation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

//Класс реализующий логику аннатации @CountDigits
public class CountDigitsValidator implements ConstraintValidator<CountDigits, Long> {

    @Override
    public boolean isValid(Long value, ConstraintValidatorContext constraintValidatorContext) {

        return String.valueOf(value).length() == 13;
    }
}
