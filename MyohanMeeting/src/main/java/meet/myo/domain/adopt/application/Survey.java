package meet.myo.domain.adopt.application;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Getter
public class Survey {

    @NonNull
    private String surveyAnswer1;

    @NonNull
    private String surveyAnswer2;

    @NonNull
    private String surveyAnswer3;

    @NonNull
    private String surveyAnswer4;

    @NonNull
    private String surveyAnswer5;

    @NonNull
    private String surveyAnswer6;
}
