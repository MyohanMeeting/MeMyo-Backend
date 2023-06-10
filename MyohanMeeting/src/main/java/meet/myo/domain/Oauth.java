package meet.myo.domain;

import jakarta.persistence.Column;
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

    @Column(nullable = false)
    private OauthType oauthType;

    @Column(nullable = false)
    private String oauthId;

    public static Oauth createOauth(OauthType oauthType, String oauthId) {
        return new Oauth(oauthType, oauthId);
    }
}
