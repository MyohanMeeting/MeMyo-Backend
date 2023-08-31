package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Member;
import meet.myo.domain.adopt.application.*;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.dto.request.adopt.AdoptApplicationCreateRequestDto;
import meet.myo.dto.request.adopt.AdoptApplicationUpdateRequestDto;
import meet.myo.dto.response.adopt.AdoptApplicationResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.AdoptApplicationRepository;
import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AdoptApplicationService {
    private final AdoptNoticeRepository adoptNoticeRepository;
    private final AdoptApplicationRepository adoptApplicationRepository;
    private final MemberRepository memberRepository;

    /**
     * 특정 공고에 달린 분양신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptApplicationResponseDto> getAdoptApplicationListByNotice(Long memberId, Long noticeId, Pageable pageable, String ordered) {
        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(noticeId)
                .orElseThrow(() -> new NotFoundException("조회된 분양 글이 없습니다."));

        // 작성자만 열람 가능한지 확인
        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("작성자만 분양신청 목록을 열람할 수 있습니다.");
        }

        Page<AdoptApplication> adoptApplications = adoptApplicationRepository.findByAdoptNoticeIdAndDeletedAtNull(adoptNotice.getId(), pageable);

        return adoptApplications.getContent().stream()
                .map(AdoptApplicationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 내가 작성한 분양신청 목록 조회
     */
    @Transactional(readOnly = true)
    public List<AdoptApplicationResponseDto> getMyAdoptApplicationList(Long memberId, Pageable pageable, String ordered) {
        Page<AdoptApplication> myApplications = adoptApplicationRepository.findByMemberIdAndDeletedAtNull(memberId, pageable);
        return myApplications.getContent().stream()
                .map(AdoptApplicationResponseDto::fromEntity)
                .collect(Collectors.toList());
    }




    /**
     * 단일 조회
     */
    @Transactional(readOnly = true)
    public AdoptApplicationResponseDto getAdoptApplication(Long applicationId) {
        AdoptApplication adoptApplication = adoptApplicationRepository.findByIdAndDeletedAtNull(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));
        return AdoptApplicationResponseDto.fromEntity(adoptApplication);
    }



    /**
     * 작성
     */
    public Long createAdoptApplication(Long memberId, AdoptApplicationCreateRequestDto dto) {
        Member member = memberRepository.findByIdAndDeletedAtNull(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        AdoptNotice adoptNotice = adoptNoticeRepository.findByIdAndDeletedAtNull(dto.getNoticeId())
                .orElseThrow(() -> new NotFoundException("Adopt notice not found"));

        Applicant applicant = Applicant.builder()
                .name(dto.getApplicant().getName())
                .age(dto.getApplicant().getAge())
                .gender(Gender.valueOf(dto.getApplicant().getGender()))
                .address(dto.getApplicant().getAddress())
                .phoneNumber(dto.getApplicant().getPhoneNumber())
                .job(dto.getApplicant().getJob())
                .married(Married.valueOf(dto.getApplicant().getMarried()))
                .build();

        //TODO: Enum NPE 처리
        Survey survey = Survey.builder()
                .surveyAnswer1of1(YesOrNo.valueOf(dto.getSurvey().getAnswer1_1()))
                .surveyAnswer1of2(dto.getSurvey().getAnswer1_2())
                .surveyAnswer2of1(YesOrNo.valueOf(dto.getSurvey().getAnswer2_1()))
                .surveyAnswer2of2(dto.getSurvey().getAnswer2_2())
                .surveyAnswer3(dto.getSurvey().getAnswer3())
                .surveyAnswer4(YesOrNo.valueOf(dto.getSurvey().getAnswer4()))
                .surveyAnswer5(dto.getSurvey().getAnswer5())
                .surveyAnswer6(YesOrNo.valueOf(dto.getSurvey().getAnswer6()))
                .build();

        AdoptApplication adoptApplication = AdoptApplication.builder()
                .member(member)
                .adoptNotice(adoptNotice)
                .applicant(applicant)
                .survey(survey)
                .content(dto.getContent())
                .build();

        adoptApplicationRepository.save(adoptApplication);
        adoptApplication.getAdoptNotice().addApplication();

        return adoptApplication.getId();
    }

    /**
     * 수정
     */
    public AdoptApplicationResponseDto updateAdoptApplication(Long memberId, Long applicationId, AdoptApplicationUpdateRequestDto dto) {
        AdoptApplication adoptApplication = adoptApplicationRepository.findByIdAndDeletedAtNull(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));

        if (!adoptApplication.getMember().getId().equals(memberId)) {
            throw new AccessDeniedException("수정할 권한이 없습니다.");
        }

        // 신청자 정보 수정
        if (dto.getApplicant().isPresent()) {
            AdoptApplicationUpdateRequestDto.Applicant applicantDto = dto.getApplicant().get();
            Applicant applicant = adoptApplication.getApplicant();

            if (applicantDto.getName().isPresent()) { applicant.updateName(applicantDto.getName().get()); }
            if (applicantDto.getAge().isPresent()) { applicant.updateAge(applicantDto.getAge().get()); }
            if (applicantDto.getGender().isPresent() && applicantDto.getGender().get() != null) {
                applicant.updateGender(Gender.valueOf(applicantDto.getGender().get()));
            }
            if (applicantDto.getAddress().isPresent()) { applicant.updateAddress(applicantDto.getAddress().get()); }
            if (applicantDto.getPhoneNumber().isPresent()) { applicant.updatePhoneNumber(applicantDto.getPhoneNumber().get()); }
            if (applicantDto.getJob().isPresent()) { applicant.updateJob(applicantDto.getJob().get()); }
            if (applicantDto.getMarried().isPresent() && applicantDto.getMarried().get() != null) {
                applicant.updateMarried(Married.valueOf(applicantDto.getMarried().get()));
            }
        }

        // 설문조사 정보 수정
        if (dto.getSurvey().isPresent()) {
            AdoptApplicationUpdateRequestDto.Survey surveyDto = dto.getSurvey().get();
            Survey survey = adoptApplication.getSurvey();

            if (surveyDto.getAnswer1_1().isPresent() && surveyDto.getAnswer1_1().get() != null) {
                survey.updateSurveyAnswer1of1(YesOrNo.valueOf(surveyDto.getAnswer1_1().get().toUpperCase()));
            }

            if (surveyDto.getAnswer1_2().isPresent()) {
                survey.updateSurveyAnswer1of2(surveyDto.getAnswer1_2().get());
            }

            if (surveyDto.getAnswer2_1().isPresent() && surveyDto.getAnswer2_1().get() != null) {
                survey.updateSurveyAnswer2of1(YesOrNo.valueOf(surveyDto.getAnswer1_1().get().toUpperCase()));
            }

            if (surveyDto.getAnswer2_2().isPresent()) {
                survey.updateSurveyAnswer2of2(surveyDto.getAnswer2_2().get());
            }

            if (surveyDto.getAnswer3().isPresent()) {
                survey.updateSurveyAnswer3(surveyDto.getAnswer3().get());
            }

            if (surveyDto.getAnswer4().isPresent() && surveyDto.getAnswer4().get() != null) {
                survey.updateSurveyAnswer4(YesOrNo.valueOf(surveyDto.getAnswer4().get().toUpperCase()));
            }

            if (surveyDto.getAnswer5().isPresent()) {
                survey.updateSurveyAnswer5(surveyDto.getAnswer5().get());
            }

            if (surveyDto.getAnswer6().isPresent() && surveyDto.getAnswer6().get() != null) {
                survey.updateSurveyAnswer6(YesOrNo.valueOf(surveyDto.getAnswer6().get().toUpperCase()));
            }
        }

        if (dto.getContent().isPresent()) {
            adoptApplication.updateContent(dto.getContent().get());
        }

        return AdoptApplicationResponseDto.fromEntity(adoptApplication);
    }



    /**
     * 삭제
     */
    public Long deleteAdoptApplication(Long memberId, Long applicationId) {
        AdoptApplication adoptApplication = adoptApplicationRepository.findByIdAndDeletedAtNull(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));

        if (!adoptApplication.getMember().getId().equals(memberId)) {
            throw new NotFoundException("삭제할 권한이 없습니다.");
        }

        adoptApplication.delete();
        adoptApplication.getAdoptNotice().removeApplication();
        return adoptApplication.getId();
    }
}
