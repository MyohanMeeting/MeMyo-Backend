package meet.myo.service;

import lombok.RequiredArgsConstructor;
import meet.myo.domain.Member;
import meet.myo.domain.adopt.application.*;
import meet.myo.domain.adopt.notice.AdoptNotice;
import meet.myo.dto.request.adopt.AdoptApplicationRequestDto;
import meet.myo.dto.response.adopt.AdoptApplicationResponseDto;
import meet.myo.exception.NotFoundException;
import meet.myo.repository.AdoptApplicationRepository;
import meet.myo.repository.AdoptNoticeRepository;
import meet.myo.repository.MemberRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.naming.AuthenticationException;
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
    public List<AdoptApplicationResponseDto> getAdoptApplicationListByNotice(Long memberId, Long noticeId, Pageable pageable, String ordered) throws AuthenticationException {
        AdoptNotice adoptNotice = (AdoptNotice) adoptNoticeRepository.findByAdoptNoticeId(noticeId)
                .orElseThrow(() -> new NotFoundException("조회된 분양 글이 없습니다."));

        // 작성자만 열람 가능한지 확인
        if (!adoptNotice.getMember().getId().equals(memberId)) {
            throw new AuthenticationException("작성자만 분양신청 목록을 열람할 수 있습니다.");
        }

        Page<AdoptApplication> adoptApplications = adoptApplicationRepository.findByAdoptNoticeIdAndDeletedAtNull(adoptNotice, pageable);

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
        AdoptApplication adoptApplication = adoptApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));
        return AdoptApplicationResponseDto.fromEntity(adoptApplication);
    }



    /**
     * 작성
     */
    public Long createAdoptApplication(Long memberId, AdoptApplicationRequestDto dto) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException("Member not found"));

        AdoptNotice adoptNotice = adoptNoticeRepository.findById(dto.getNoticeId())
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
                .content(dto.getContent())
                .adoptNotice(adoptNotice)
                .applicant(applicant)
                .survey(survey)
                .build();

        adoptApplicationRepository.save(adoptApplication);
        return adoptApplication.getId();
    }

    /**
     * 수정
     */
    public AdoptApplicationResponseDto updateAdoptApplication(Long memberId, Long applicationId, AdoptApplicationRequestDto dto) {
        AdoptApplication adoptApplication = adoptApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));

        if (!adoptApplication.getMember().getId().equals(memberId)) {
            throw new NotFoundException("수정할 권한이 없습니다.");
        }

        Applicant applicant = Applicant.builder()
                .name(dto.getApplicant().getName())
                .age(dto.getApplicant().getAge())
                .gender(dto.getApplicant().getGender() != null ? Gender.valueOf(dto.getApplicant().getGender()) : adoptApplication.getApplicant().getGender())
                .address(dto.getApplicant().getAddress())
                .phoneNumber(dto.getApplicant().getPhoneNumber())
                .job(dto.getApplicant().getJob())
                .married(dto.getApplicant().getMarried() != null ? Married.valueOf(dto.getApplicant().getMarried()) : adoptApplication.getApplicant().getMarried())
                .build();

        Survey survey = Survey.builder()
                .surveyAnswer1of1(dto.getSurvey().getAnswer1_1() != null ? YesOrNo.valueOf(dto.getSurvey().getAnswer1_1().toUpperCase()) : adoptApplication.getSurvey().getSurveyAnswer1of1())
                .surveyAnswer1of2(dto.getSurvey().getAnswer1_2() != null ? dto.getSurvey().getAnswer1_2() : adoptApplication.getSurvey().getSurveyAnswer1of2())
                .surveyAnswer2of1(dto.getSurvey().getAnswer2_1() != null ? YesOrNo.valueOf(dto.getSurvey().getAnswer2_1().toUpperCase()) : adoptApplication.getSurvey().getSurveyAnswer2of1())
                .surveyAnswer2of2(dto.getSurvey().getAnswer2_2() != null ? dto.getSurvey().getAnswer2_2() : adoptApplication.getSurvey().getSurveyAnswer2of2())
                .surveyAnswer3(dto.getSurvey().getAnswer3() != null ? dto.getSurvey().getAnswer3() : adoptApplication.getSurvey().getSurveyAnswer3())
                .surveyAnswer4(dto.getSurvey().getAnswer4() != null ? YesOrNo.valueOf(dto.getSurvey().getAnswer4().toUpperCase()) : adoptApplication.getSurvey().getSurveyAnswer4())
                .surveyAnswer5(dto.getSurvey().getAnswer5() != null ? dto.getSurvey().getAnswer5() : adoptApplication.getSurvey().getSurveyAnswer5())
                .surveyAnswer6(dto.getSurvey().getAnswer6() != null ? YesOrNo.valueOf(dto.getSurvey().getAnswer6().toUpperCase()) : adoptApplication.getSurvey().getSurveyAnswer6())
                .build();

        if (dto.getContent() != null) {
            adoptApplication.updateContent(dto.getContent());
        }

        adoptApplicationRepository.save(adoptApplication);
        return AdoptApplicationResponseDto.fromEntity(adoptApplication);
    }



    /**
     * 삭제
     */
    public Long deleteAdoptApplication(Long memberId, Long applicationId) {
        AdoptApplication adoptApplication = adoptApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new NotFoundException("해당하는 분양신청이 존재하지 않습니다."));

        if (!adoptApplication.getMember().getId().equals(memberId)) {
            throw new NotFoundException("삭제할 권한이 없습니다.");
        }

        adoptApplication.delete();

        return adoptApplication.getId();
    }
}
