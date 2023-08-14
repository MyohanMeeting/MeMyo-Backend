package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.UUID;

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
    private Member member;

    @Column(nullable = false)
    private String certCode;

    private EmailCertification(Member member) {
        this.member = member;
        this.certCode = createCertCode();
    }

    public static EmailCertification createEmailCertification(Member member) {
        return new EmailCertification(member);
    }

    private String createCertCode() {
        Random random = new Random();
        int min = 100000;
        int max = 999999;
        return String.valueOf(random.nextInt(max - min + 1) + min);
    }

    public boolean isExpired() {
        return Duration.between(super.getCreatedAt(), LocalDateTime.now())
                .compareTo(Duration.ofSeconds(3000))  // TODO: 임의로 만료시간 5분 설정
                >= 0;
    }
}
