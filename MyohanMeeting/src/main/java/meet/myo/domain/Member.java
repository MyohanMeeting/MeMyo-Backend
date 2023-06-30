package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.authority.MemberAuthority;
import org.springframework.dao.DuplicateKeyException;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(nullable = false)
    private String email;

    private String password;

    private String nickname;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Upload profileImage;

    private String phoneNumber;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Certified certified; // CERTIFIED, NOT_CERTIFIED

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "member")
    private List<MemberAuthority> memberAuthorities;

    @Embedded
    private Oauth oauth;

    @Builder(builderClassName = "DirectJoinMemberBuilder", builderMethodName = "directJoinBuilder")
    Member(String email, String password, String nickname, String phoneNumber) {
        this.email = email;
        this.password = password;
        this.nickname = nickname;
        this.phoneNumber = phoneNumber;
        this.certified = Certified.NOT_CERTIFIED; // 미인증을 기본값으로 세팅
    }

    @Builder(builderClassName = "OauthJoinMemberBuilder", builderMethodName = "oauthJoinBuilder")
    Member(OauthType oauthType, String oauthId, String email, String nickname) {
        this.email = email;
        this.oauth = Oauth.createOauth(oauthType, oauthId);
        this.nickname = nickname;
        this.certified = Certified.NOT_CERTIFIED; // 미인증을 기본값으로 세팅
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateProfileImage(Upload upload) {
        this.profileImage = upload;
    }

    public void updatePhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updateCertified() {
        if (this.certified == Certified.NOT_CERTIFIED) {
            this.certified = Certified.CERTIFIED;
        } else {
            throw new DuplicateKeyException("ALREADY CERTIFIED");
        }
    }

    public void updateOauth(Oauth updatedOauth) {
        this.oauth = updatedOauth;
    }

    public void delete() {
        super.delete();
    }

}
