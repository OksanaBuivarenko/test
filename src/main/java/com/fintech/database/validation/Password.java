package com.fintech.database.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({FIELD})
@Retention(RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
@Documented
public @interface Password {
    String message() default "Неверный формат пароля. Пароль должен состоять из букв, цифр и символов, " +
            "обязательно содержать заглавную латинскую букву, цифру и иметь длину не менее 8 символов";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}