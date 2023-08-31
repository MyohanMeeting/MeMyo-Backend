package meet.myo.domain.authority;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

public class CustomUser extends User {

    private Long userPK;

    public CustomUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long userPK) {
        super(username, password, authorities);
        this.userPK = userPK;
    }

    public CustomUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities, Long userPK) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
        this.userPK = userPK;
    }

    public Long getUserPK() {
        return this.userPK;
    }
}
