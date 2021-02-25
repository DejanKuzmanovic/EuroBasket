package org.unibl.eurobasket.validation;

import org.unibl.eurobasket.dto.MatchingPasswordDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PasswordConstraintValidator implements ConstraintValidator<ValidPassword, Object> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o instanceof MatchingPasswordDTO) {
            MatchingPasswordDTO dto = (MatchingPasswordDTO) o;
            return dto.getPassword().equals(dto.getMatchingPassword());
        }
        return false;
    }
}
