package meet.myo.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class AdoptApplication {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adopt_application_id")
    private Long id;

    private Member member;

    private String content;

    @ManyToOne
    @JoinColumn(name = "adopt_notice_id")
    private AdoptNotice adoptNotice;

}