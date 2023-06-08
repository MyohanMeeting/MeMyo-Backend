package meet.myo.domain.cat;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import meet.myo.domain.BaseAuditingListener;
import meet.myo.domain.Upload;

//MEMO: 실제 cat, upload 간의 관계를 rdb에서 표현하려면 중간 테이블이 존재해야 하므로 CatPictures라는 클래스로 풀어냈습니다.
//  cat_pictures는 cat, upload의 외래키만 갖고 있는 연결 테이블입니다.
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CatPicture extends BaseAuditingListener {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cat_pictures_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cat_id")
    private Cat cat;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "upload_id")
    private Upload upload;

    public static CatPicture createCatPicture(Cat cat, Upload upload) {
        CatPicture catPicture = new CatPicture();
        catPicture.cat = cat;
        catPicture.upload = upload;
        return catPicture;
    }
}
