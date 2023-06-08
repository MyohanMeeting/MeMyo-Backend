package meet.myo.domain.adopt.application;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Member;
import meet.myo.domain.adopt.notice.AdoptNotice;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptApplication extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_application_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne
    @JoinColumn(name = "adopt_notice_id")
    private AdoptNotice adoptNotice;

    // 신청인 정보
    @Embedded
    private Applicant applicant;

    // 설문조사
    @Embedded
    private Survey survey;

    // 규정동의
    @Embedded
    private Agreement agreement;

    @Builder
    AdoptApplication(Member member, String content, AdoptNotice adoptNotice, Applicant applicant, Survey survey) {
        this.member = member;
        this.content = content;
        this.adoptNotice = adoptNotice;
        this.applicant = applicant;
        this.survey = survey;
        this.agreement = agreement;
    }
}
