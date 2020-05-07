package com.datn.onlinejobportal.constraint;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import com.datn.onlinejobportal.payload.ResetPasswordRequest;

public class PasswordMatchesValidator implements ConstraintValidator<PasswordMatches, Object> {

    @Override
    public void initialize(final PasswordMatches constraintAnnotation) {
        //
    }

    @Override
    public boolean isValid(final Object obj, final ConstraintValidatorContext context) {
        final ResetPasswordRequest reset = (ResetPasswordRequest) obj;
        return reset.getNewpassword().equals(reset.getReenterednewpassword());
    }

}
