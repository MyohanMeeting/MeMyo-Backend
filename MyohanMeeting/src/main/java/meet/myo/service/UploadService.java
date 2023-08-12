package meet.myo.service;

import lombok.RequiredArgsConstructor;

import meet.myo.domain.FileCategory;
import meet.myo.domain.Member;
import meet.myo.domain.Upload;
import meet.myo.exception.NotFoundException;
import meet.myo.dto.response.UploadResponseDto;
import meet.myo.repository.MemberRepository;
import meet.myo.repository.UploadRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final CloudStorageService fileUploader;
    private final UploadRepository uploadRepository;
    private final MemberRepository memberRepository;

    /**
     * 파일 상세 조회
     */
    @Transactional(readOnly = true)
    public UploadResponseDto getFileDetail(Long memberId, Long id) {
        Upload upload = uploadRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));
        if (!upload.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("파일을 열람할 권한이 없습니다.");
        }
        return UploadResponseDto.fromEntity(upload);
    }

    /**
     * 파일 업로드
     */
    public List<Long> uploadFiles(Long memberId, List<MultipartFile> files, String category) {

        // 파일 카테고리 Enum화
        FileCategory fileCategory = FileCategory.valueOf(category);

        // member 엔티티 영속화
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        // Upload 엔티티 담을 리스트 생성
        List<Upload> uploadList = new ArrayList<>();

        // 업로드 엔티티 생성 및 파일 업로드
        //TODO: 유효성 검증(파일 용량, 확장자, 10건 이상 업로드 불가 등)
        for (MultipartFile file : files) {
            if (file.isEmpty()) {
                throw new IllegalArgumentException("파일이 존재하지 않습니다.");
            }

            // 유니크 파일명 생성 및 기존 파일명 저장
            String originName = file.getOriginalFilename() == null ? "tmp" : file.getOriginalFilename();
            String savedName = generateSavedName(originName);

            // 카테고리 포함한 파일 패스 및 url 생성
            String filePath = fileCategory.toString().toLowerCase() + "/" + savedName;
            String savedUrl = fileUploader.getUrl(filePath);

            // 업로드 엔티티 생성 및 리스트에 추가
            Upload upload = Upload.builder()
                .member(member)
                .fileCategory(fileCategory)
                .url(savedUrl)
                .originName(originName)
                .savedName(savedName)
                .type(file.getContentType()) // MIME 타입
                .extension(getFileExtension(originName))
                .size(file.getSize())
                .build();

            uploadList.add(upload);

            // 엔티티가 무사히 생성되면 파일 업로드
            try {
                fileUploader.doUpload(file, filePath);
            } catch (IOException e) {
                // TODO: 예외처리
            }
        }

        // TODO: 업로드 중간에 오류가 발생할 경우 이미 업로드된 파일 처리 방안 생각하기
        // 배열로 업로드하는 메서드가 있지않을까..?

        return uploadRepository.saveAll(uploadList).stream().map(Upload::getId).toList();
    }

    /**
     * 파일 삭제
     */
    public List<Long> deleteFiles(Long memberId, List<Long> uploadIdList) {
        List<Upload> uploadList = uploadRepository.findAllById(uploadIdList);
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        //TODO: 쿼리 최적화
        if (uploadList.stream().noneMatch(u -> u.getMember().equals(member))) {
            throw new AccessDeniedException("파일을 삭제할 권한이 없습니다.");
        }

        // 처리 도중 예외가 발생할 경우 bucket에 파일 삭제 요청을 보내지 않기 위해 마지막에 삭제
        List<String> deleteUrls = new ArrayList<>();

        for (Upload upload : uploadList) {
            upload.delete();
            deleteUrls.add(upload.getUrl());
        }
        deleteUrls.forEach(fileUploader::doDelete);

        return uploadList.stream().map(Upload::getId).toList();
    }

    /**
     * 랜덤 파일명 생성
     */
    private String generateSavedName(String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(originalFilename);
        return uniqueId + "-" + System.currentTimeMillis() + "." + fileExtension;
    }

    /**
     * 파일 확장자 추출
     */
    private String getFileExtension(String originalFilename) {
        int dotIndex = originalFilename.lastIndexOf('.');
        assert dotIndex >= 0 && dotIndex < originalFilename.length() - 1;

        return originalFilename.substring(dotIndex + 1).toLowerCase();
    }
}
