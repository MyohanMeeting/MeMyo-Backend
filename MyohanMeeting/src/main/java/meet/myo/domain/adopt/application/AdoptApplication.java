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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_notice_id")
    private AdoptNotice adoptNotice;

    @Embedded
    private Applicant applicant;

    @Embedded
    private Survey survey;

    /**
     * 문항에 하나라도 동의하지 않으면 신청서 작성이 불가능하기 때문에,
     * 전체 동의 여부를 나타내는 필드만 생성하고 이를 동의 상태로 초기화합니다.
     */
    private String isAllAgreed = "AGREED";

    @Builder
    AdoptApplication(Member member, String content, AdoptNotice adoptNotice, Applicant applicant, Survey survey) {
        this.member = member;
        this.content = content;
        this.adoptNotice = adoptNotice;
        this.applicant = applicant;
        this.survey = survey;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void delete() {
        super.delete();
    }

}
