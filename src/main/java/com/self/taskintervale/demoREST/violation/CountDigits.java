package com.self.taskintervale.demoREST.violation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

// Самопальная аннатация валидирующая значение типа long , валидация пройдена успешно если число состоит из 13 цифр.
@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = CountDigitsValidator.class)
@Documented
public @interface CountDigits {
    String message() default "number must contain 13 digits";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
