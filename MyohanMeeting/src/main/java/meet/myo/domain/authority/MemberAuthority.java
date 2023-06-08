package meet.myo.domain.authority;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Member;


// MEMO: Member와 Authority간의 다대다 연관관계를 풀어주는 중간테이블입니다.

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class MemberAuthority extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_authority_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "authority_id")
    private Authority authority;

    private MemberAuthority(Member member, Authority authority) {
        this.member = member;
        this.authority = authority;
    }

    public static MemberAuthority createMemberAuthority(Member member, Authority authority) {
        return new MemberAuthority(member, authority);
    }

    /**
     * 권한 부여
     */
    public static MemberAuthority addAuthority(Member member, Authority authority) {
        return new MemberAuthority(member, authority);
    }

    @Override
    public String toString() {
        return this.authority.getAuthorityName();
    }

    public Authority toAuthority() {
        return this.authority;
    }

    public void delete() {
        super.delete();
    }
}
