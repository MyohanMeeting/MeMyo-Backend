package meet.myo.util.validation.pattern;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Constraint(validatedBy = CustomPatternValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CustomPattern {
    String message() default "{validation.Pattern}";
    CustomPatternRegexp regexp();
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}