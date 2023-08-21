package meet.myo.util.validation.enums;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class EnumValidator implements ConstraintValidator<EnumValid, String> {

    private EnumValid annotation;

    @Override
    public void initialize(EnumValid constraintAnnotation) {
        this.annotation = constraintAnnotation;
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }

        Object[] enumValues = this.annotation.enumClass().getEnumConstants();
        if (enumValues != null) {
            for (Object e : enumValues) {
                if (e.toString().equals(value)) {
                    return true;
                }
            }
        }

        return false;
    }
}
