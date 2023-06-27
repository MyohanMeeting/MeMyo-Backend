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
import meet.myo.domain.adopt.notice.cat.Cat;
import meet.myo.domain.adopt.notice.cat.CatPicture;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptNotice extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_notice_id")
    private Long id;

    @Embedded
    private Cat cat;

    @Embedded
    private Shelter shelter;

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
    private List<CatPicture> catPictures;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptNoticeComment> commentList;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptApplication> applicationList;

    private Integer applicationCount;

    private Integer commentCount;

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

    public void updateThumbnail(Upload upload) {
        this.thumbnail = upload;
    }

    public void updateNoticeStatus(AdoptNoticeStatus status) {
        this.noticeStatus = status;
    }

    /**
     * TODO: updateCat
     */
    public void updateCat(Cat cat) { this.cat = cat; }

    public void updateShelter(Shelter shelter) { this.shelter = shelter; }

    public void addApplication() {
        applicationCount ++;
    }

    public void removeApplication() {
        applicationCount --;
    }

    public void addComment() {
        commentCount ++;
    }

    public void removeComment() {
        commentCount --;
    }

}
