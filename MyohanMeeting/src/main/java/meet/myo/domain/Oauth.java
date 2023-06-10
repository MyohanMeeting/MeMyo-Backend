package meet.myo.domain;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

    private OauthType oauthType;
    private String oauthId;

    public static Oauth createOauth(OauthType oauthType, String oauthId) {
        return new Oauth(oauthType, oauthId);
    }
}
