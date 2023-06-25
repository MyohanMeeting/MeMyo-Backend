package meet.myo.domain.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User extends DefaultOAuth2User {

    private Long userPK;

    public CustomOAuth2User(Collection<? extends GrantedAuthority> authorities, Map<String, Object> attributes, String nameAttributeKey, Long userPK) {
        super(authorities, attributes, nameAttributeKey);
        this.userPK = userPK;
    }

    public Long getUserPK() {
        return this.userPK;
    }
}
