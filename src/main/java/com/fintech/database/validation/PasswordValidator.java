package com.fintech.database.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<Password, String> {

    @Override
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        if (password != null) {
            return password.matches("^(?=.*\\d)(?=.*[A-Z]).{8,}$");
        } else {
            return false;
        }
    }
}