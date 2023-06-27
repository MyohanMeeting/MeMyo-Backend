package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.application.AdoptApplication;
import meet.myo.dto.response.AuthorResponseDto;

import java.time.LocalDateTime;

@Schema(name = "AdoptApplication")
@Getter
public class AdoptApplicationResponseDto {

    private Long applicationId;
    private Long noticeId;
    private AuthorResponseDto author;
    private ApplicantResponseDto applicant;
    private SurveyResponseDto survey;
    private String content;
    private LocalDateTime createdAt;
    public static AdoptApplicationResponseDto fromEntity(AdoptApplication entity) {
        AdoptApplicationResponseDto dto = new AdoptApplicationResponseDto();
        dto.applicationId = entity.getId();
        dto.noticeId = entity.getAdoptNotice().getId();
        dto.author = AuthorResponseDto.fromEntity(entity.getMember());
        dto.applicant = ApplicantResponseDto.fromEntity(entity.getApplicant());
        dto.survey = SurveyResponseDto.fromEntity(entity.getSurvey());
        dto.content = entity.getContent();
        dto.createdAt = entity.getCreatedAt();
        return dto;
    }
}
