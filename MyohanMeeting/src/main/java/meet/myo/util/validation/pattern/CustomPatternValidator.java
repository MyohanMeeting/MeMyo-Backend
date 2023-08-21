package meet.myo.util.validation.pattern;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;


public class CustomPatternValidator implements ConstraintValidator<CustomPattern, String> {

    private CustomPatternRegexp regexp;

    @Override
    public void initialize(CustomPattern constraintAnnotation) {
        this.regexp = constraintAnnotation.regexp();
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        return Pattern.matches(regexp.getValue(), value);
    }
}
