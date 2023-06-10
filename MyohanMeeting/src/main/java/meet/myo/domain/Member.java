package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.dto.request.MemberOauthCreateRequestDto;

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

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickName;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profile_image_id")
    private Upload profileImage;

    @Column(nullable = false)
    private String phoneNumber;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Certified certified; // CERTIFIED, NOT_CERTIFIED

    @Embedded
    private Oauth oauth;

    @Builder
    Member(String email, String name, String password, String nickName, String phoneNumber, Oauth oauth) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
        this.certified = Certified.NOT_CERTIFIED; // 미인증을 기본값으로 세팅
        this.oauth = oauth;

    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updatePassword(String password) {
        this.password = password;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
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
            // TODO: 이미 인증된 회원임을 알리는 로직이 필요할까요?
            // conflict
        }
    }

    public static Member createMember(MemberOauthCreateRequestDto dto) {
        Oauth oauth = Oauth.createOauth(OauthType.valueOf(dto.getOauthType()), dto.getOauthId());
        return Member.builder()
                .email(dto.getEmail())
                .oauth(oauth)
                .build();
    }

    public void updateOauth(Oauth updatedOauth) {
        this.oauth = updatedOauth;
    }

    public void delete() {
        super.delete();
    }

}
