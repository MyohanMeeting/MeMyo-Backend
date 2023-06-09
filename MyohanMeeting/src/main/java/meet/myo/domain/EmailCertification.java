package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class EmailCertification extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "email_certification_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    @Column(nullable = false)
    private Member member;

    @Column(nullable = false)
    private String UUID;

    private EmailCertification(Member member) {
        this.member = member;
        this.UUID = createUUID();
    }

    public static EmailCertification createEmailCertification(Member member) {
        return new EmailCertification(member);
    }

    private String createUUID() {
        return "";
    } //TODO: 로직 짜기

    private boolean isExpired() {
        return Duration.between(super.getCreatedAt(), LocalDateTime.now())
                .compareTo(Duration.ofSeconds(3000))  // TODO: 임의로 만료시간 5분 설정
                >= 0;
    }
}
