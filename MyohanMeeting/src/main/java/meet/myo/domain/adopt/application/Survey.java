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

    public void updateSurveyAnswer1of1(YesOrNo answer) {
        this.surveyAnswer1of1 = answer;
    }

    public void updateSurveyAnswer1of2(String answer) {
        this.surveyAnswer1of2 = answer;
    }

    public void updateSurveyAnswer2of1(YesOrNo answer) {
        this.surveyAnswer2of1 = answer;
    }

    public void updateSurveyAnswer2of2(String answer) {
        this.surveyAnswer2of2 = answer;
    }

    public void updateSurveyAnswer3(String answer) {
        this.surveyAnswer3 = answer;
    }

    public void updateSurveyAnswer4(YesOrNo answer) {
        this.surveyAnswer4 = answer;
    }

    public void updateSurveyAnswer5(String answer) {
        this.surveyAnswer5 = answer;
    }

    public void updateSurveyAnswer6(YesOrNo answer) {
        this.surveyAnswer6 = answer;
    }
}
