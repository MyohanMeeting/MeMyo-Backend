package meet.myo.domain.adopt.notice;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Member;
import meet.myo.domain.Upload;
import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.domain.cat.Cat;
import meet.myo.dto.schema.CatRequestSchema;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptNotice extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_notice_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id")
    private Member member;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id")
    private Upload thumbnail;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptNoticeComment> commentList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptApplication> applicationList;

    private Integer applicationCount;

    // 접수중 상태로 초기화
    @Enumerated(EnumType.STRING)
    private AdoptNoticeStatus noticeStatus = AdoptNoticeStatus.ACCEPTING;

    @Builder
    AdoptNotice(Cat cat, Member member, String title, String content) {
        this.cat = cat;
        this.member = member;
        this.title = title;
        this.content = content;
    }

    public void updateTitle(String title) {
        this.title = title;
    }

    public void updateContent(String content) {
        this.content = content;
    }

    public void updateNoticeStatus(AdoptNoticeStatus status) {
        this.noticeStatus = status;
    }

    public void addApplication() {
        applicationCount ++;
    }

    public void removeApplication() {
        applicationCount --;
    }

    /**
     * TODO: updateCat
     */
    public void updateCat(CatRequestSchema catRequestSchema) {}
}
