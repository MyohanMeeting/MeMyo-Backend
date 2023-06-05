package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member extends BaseAuditingListener {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;

    private String password;

    private String nickName;

    @Column(unique = true)
    private String phoneNumber;

    private String refreshToken;

    //TODO: Upload 클래스에 빌더 패턴을 적용하고 보니 Member 클래스도 스트링 필드가 여러번 중복되어서...빌더 패턴으로 변경하는 게 좋을까요?
    private Member(String email, String name, String password, String nickName, String phoneNumber) {
        this.email = email;
        this.name = name;
        this.password = password;
        this.nickName = nickName;
        this.phoneNumber = phoneNumber;
    }

    public static Member createMember(String email, String name, String password, String nickName, String phoneNumber) {
        return new Member(email, name, password, nickName, phoneNumber);
    }

    public void updateEmail(String email) {
        this.email = email;
    }

    public void updateName(String name) {
        this.name = name;
    }

    public void updateNickName(String nickName) {
        this.nickName = nickName;
    }
    public void updatePhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public void updateRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void updatePassword(String password) {
        this.password = password;
    }
}
