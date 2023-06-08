package meet.myo.domain.authority;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Authority extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "authority_id")
    private Long id;

    @Column(nullable = false)
    private String authorityName;

    public static Authority createAuthority(String authorityName) {
        Authority authority = new Authority();
        authority.authorityName = authorityName;
        return authority;
    }

    public void updateAuthorityName(String authorityName) {
        this.authorityName = authorityName;
    }

    @Override
    public String toString() {
        return authorityName;
    }
}
