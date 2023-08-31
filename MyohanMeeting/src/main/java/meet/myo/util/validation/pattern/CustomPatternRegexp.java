package meet.myo.util.validation.pattern;

import lombok.Getter;

@Getter
public enum CustomPatternRegexp {
    PASSWORD("(?=.*\\d)(?=.*\\w).*"),
    PHONE_NUMBER("0(\\d){1,2}-(\\d){3,4}-(\\d){4}");

    private final String value;

    CustomPatternRegexp(String value) {
        this.value = value;
    }
}
