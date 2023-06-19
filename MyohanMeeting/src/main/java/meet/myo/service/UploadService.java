package meet.myo.service;

import lombok.RequiredArgsConstructor;

import meet.myo.domain.Member;
import meet.myo.domain.Upload;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.response.UploadResponseDto;
import meet.myo.repository.MemberRepository;
import meet.myo.repository.UploadRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final UploadRepository uploadRepository;
    private final MemberRepository memberRepository;

    /**
     * 파일상세 엔티티 조회
     */
    @Transactional(readOnly = true)
    public UploadResponseDto getFileDetail(Long id) {
        Upload upload = uploadRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));
        return UploadResponseDto.fromEntity(upload);
    }
    /**
     * 파일 업로드 처리
     */
    public List<Long> uploadFiles(Long memberId, List<MultipartFile> files) {
        List<Long> uploadIds = new ArrayList<>();

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        for (MultipartFile file : files) {
            String savedName = generateSavedName(file.getOriginalFilename());
            String filePath = uploadDir + File.separator + savedName;
            // TODO: 저장될 Directory 는 yml 파일을 건드려야해서 일단 두겠습니다
            try {
                // 파일 저장
                file.transferTo(new File(filePath));

                // 업로드 정보 생성 및 저장
                Upload upload = Upload.builder()
                        .url(filePath)
                        .path(filePath)
                        .originName(file.getOriginalFilename())
                        .savedName(savedName)
                        .type(file.getContentType()) // MIME 타입
                        .extension(getFileExtension(file.getOriginalFilename()))
                        .size(file.getSize())
                        .build();

                Upload savedUpload = uploadRepository.save(upload);
                uploadIds.add(savedUpload.getId());
            } catch (IOException e) {
                //TODO: 파일 저장 중 에러 발생 시 어떻게 처리?? ex) 파일최대크기 초과..
                e.printStackTrace();
            }
        }

        return uploadIds;
    }

    /**
     * 파일 삭제 처리
     */
    public List<Long> deleteFiles(Long memberId, List<Long> uploadIdList) {
        List<Long> deletedIds = new ArrayList<>();
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        for (Long uploadId : uploadIdList) {
            Upload upload = uploadRepository.findByIdAndDeletedAtNull(uploadId)
                    .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));

            File file = new File(upload.getPath());
            if (file.exists()) {
                upload.delete();
                uploadRepository.save(upload);
            }
        }
        return deletedIds;
    }

    // 파일 저장명을 유니크하게 생성하는 메소드
    private String generateSavedName(String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(originalFilename);
        return uniqueId + "." + fileExtension;
    }

    // 파일을 저장하는 메소드
    private void saveFile(MultipartFile file, String filePath) throws IOException {
        Path targetPath = Path.of(filePath);
        Files.copy(file.getInputStream(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        // 동일한경로에 파일이 존재하면 덮어쓰기 옵션 REPLACE_EXISTING
    }

    // 파일 확장자를 추출하는 메소드
    private String getFileExtension(String originalFilename) {
        if (originalFilename != null) {
            int dotIndex = originalFilename.lastIndexOf('.');
            if (dotIndex >= 0 && dotIndex < originalFilename.length() - 1) {
                return originalFilename.substring(dotIndex + 1).toLowerCase();
            }  // dot 가 유효한 범위에 있을 때 확장자를 소문자로 반환합니다
        }
        return "";
    }
}