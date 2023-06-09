package meet.myo.domain;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@EntityListeners(AuditingEntityListener.class) //MEMO: https://ugo04.tistory.com/100, https://ugo04.tistory.com/102
public abstract class BaseAuditingListener {

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    private LocalDateTime deletedAt = null;

    /*
        엔티티를 삭제할 경우에는 super.delete(); 형태로 아래의 메서드를 상속받아 처리합니다.
     */
    protected void delete() { deletedAt = LocalDateTime.now(); }
}
