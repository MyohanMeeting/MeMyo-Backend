package meet.myo.domain;

import com.fasterxml.jackson.annotation.JacksonInject;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptNotice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_notice_id")
    private Long id;

    //한 고양이와 한 공고 매핑
    @OneToOne
    @JoinColumn(name = "cat_id")
    private Cat cat;

    //한 사람이 여러개의 공고를 낼 수 있다
    @ManyToOne
    @JoinColumn(name="member_id")
    private Member member;

    private String title;

    private String content;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "adoptNotice")
    private List<AdoptApplication> applicationList;

}
