package meet.myo.domain.adopt.application;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Getter
public class Survey {

    @Enumerated(EnumType.STRING)
    private YesOrNo surveyAnswer1of1;

    @Column(columnDefinition = "TEXT")
    private String surveyAnswer1of2;

    @Enumerated(EnumType.STRING)
    private YesOrNo surveyAnswer2of1;

    @Column(columnDefinition = "TEXT")
    private String surveyAnswer2of2;

    @Column(columnDefinition = "TEXT")
    private String surveyAnswer3;

    @Enumerated(EnumType.STRING)
    private YesOrNo surveyAnswer4;

    @Column(columnDefinition = "TEXT")
    private String surveyAnswer5;

    @Enumerated(EnumType.STRING)
    private YesOrNo surveyAnswer6;
}
