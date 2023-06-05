package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.dto.response.UploadResponseDto;
import meet.myo.repository.UploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final UploadRepository uploadRepository;

    /**
     * 파일상세 엔티티 조회
     */
    @Transactional(readOnly = true)
    public UploadResponseDto getFileDetail(Long id) {
        return UploadResponseDto.fromEntity();
    }

    /**
     * 파일 업로드 처리
     */
    public Long uploadFile(List<MultipartFile> files) {
        return 1L;
    }

    /**
     * 파일 삭제 처리
     */
    public List<Long> deleteFile(List<Long> idList) {
        return List.of(1L);
    }

}
