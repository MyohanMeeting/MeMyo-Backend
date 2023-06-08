package meet.myo.domain.adopt.notice;

import jakarta.persistence.*;
import lombok.*;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Member;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptNoticeComment extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notice_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "adopt_notice_id")
    private AdoptNotice adoptNotice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Builder
    AdoptNoticeComment(AdoptNotice adoptNotice, Member member, String content) {
        this.adoptNotice = adoptNotice;
        this.member = member;
        this.content = content;
    }
}
