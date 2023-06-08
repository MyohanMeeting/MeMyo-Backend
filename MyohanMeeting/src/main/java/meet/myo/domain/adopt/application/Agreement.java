package meet.myo.domain.adopt.application;

import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Agreement {

    /**
     * 문항에 동의하지 않으면 신청서 작성이 아예 불가능하기 때문에,
     * 어떤 방식으로든 작성이 되었다면 동의한 것으로 간주하고 값을 초기화합니다.
     */
    private String agreementAnswer1 = "AGREED";
    private String agreementAnswer2 = "AGREED";
    private String agreementAnswer3 = "AGREED";
    private String agreementAnswer4 = "AGREED";
}
