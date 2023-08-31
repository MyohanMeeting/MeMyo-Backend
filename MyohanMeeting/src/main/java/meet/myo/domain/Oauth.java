package meet.myo.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Embeddable
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Oauth {

    @Column(name = "oauth_type")
    @Enumerated(EnumType.STRING)
    private OauthType oauthType;

    @Column(name = "oauth_id")
    private String oauthId;

    public static Oauth createOauth(OauthType oauthType, String oauthId) {
        Oauth oauth = new Oauth();
        oauth.oauthType = oauthType;
        oauth.oauthId = oauthId;
        return oauth;
    }
}
