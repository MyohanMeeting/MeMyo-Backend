package meet.myo.service;

import lombok.RequiredArgsConstructor;

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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UploadService {

    private final UploadRepository uploadRepository;
    private final MemberRepository memberRepository;
    private final String bucket = "test.bucket.com";

    /**
     * 파일 상세 조회
     */
    @Transactional(readOnly = true)
    public UploadResponseDto getFileDetail(Long memberId, Long id) {
        Upload upload = uploadRepository.findByIdAndDeletedAtNull(id)
                .orElseThrow(() -> new NotFoundException("파일을 찾을 수 없습니다."));
        if (!upload.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("ACCESS DENIED");
        }
        return UploadResponseDto.fromEntity(upload);
    }

    /**
     * 파일 업로드
     */
    public List<Long> uploadFiles(Long memberId, List<MultipartFile> files) {

        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));
        List<Upload> uploadList = new ArrayList<>();

        for (MultipartFile file : files) {
            assert !file.isEmpty();
            String originName = file.getOriginalFilename() == null ? "tmp" : file.getOriginalFilename();
            String savedName = generateSavedName(originName);
            doUpload(file, savedName);

            // 업로드 정보 생성 및 저장
            Upload upload = Upload.builder()
                .member(member)
                .url(getUrl(savedName))
                .originName(originName)
                .savedName(savedName)
                .type(file.getContentType()) // MIME 타입
                .extension(getFileExtension(originName))
                .size(file.getSize())
                .build();

            uploadList.add(upload);
        }
        return uploadRepository.saveAll(uploadList).stream().map(Upload::getId).toList();
    }

    /**
     * 파일 삭제
     */
    public List<Long> deleteFiles(Long memberId, List<Long> uploadIdList) {
        List<Upload> uploadList = uploadRepository.findAllById(uploadIdList);
        List<String> deleteUrls = new ArrayList<>(); // 처리 도중 예외가 발생할 경우 cdn에 파일 삭제 요청을 보내지 않기 위해 마지막에 삭제
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("id에 해당하는 회원을 찾을 수 없습니다."));

        for (Upload upload : uploadList) {
            //TODO: 쿼리 1번으로 멤버id 전부 비교 가능할지 생각해보기
            if (!upload.getMember().equals(member)) {
                throw new AccessDeniedException("ACCESS DENIED");
            }

            upload.delete();
            deleteUrls.add(upload.getUrl());
        }

        deleteUrls.forEach(this::doDelete);
        return uploadList.stream().map(Upload::getId).toList();
    }

    /**
     * 실제 파일 업로드
     */
    private void doUpload(MultipartFile file, String savedName) {
        //TODO: S3 구현
    }

    /**
     * 실제 파일 삭제
     */
    private void doDelete(String url) {
        //TODO: S3 구현
    }

    /**
     * 랜덤 파일명 생성
     */
    private String generateSavedName(String originalFilename) {
        String uniqueId = UUID.randomUUID().toString();
        String fileExtension = getFileExtension(originalFilename);
        return uniqueId + System.currentTimeMillis() + "." + fileExtension;
    }

    /**
     * 정해진 패턴에 따라 파일 url 생성
     */
    private String getUrl(String fileName) {
        return "https://" + this.bucket + fileName;
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
