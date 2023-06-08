package meet.myo.domain.adopt.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Member;
import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.domain.cat.Cat;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptNotice extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_notice_id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptNoticeComment> commentList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptApplication> applicationList;

    private Integer applicationCount;

    @Enumerated(EnumType.STRING)
    private AdoptNoticeStatus noticeStatus;

    public void addApplication() {
        applicationCount ++;
    }

    public void removeApplication() {
        applicationCount --;
    }
}
