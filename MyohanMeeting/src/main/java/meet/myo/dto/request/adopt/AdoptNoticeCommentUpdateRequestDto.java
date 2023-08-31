package meet.myo.dto.request.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Schema(name = "adoptNoticeCommentForm")
@Getter
public class AdoptNoticeCommentUpdateRequestDto {

    @Schema(type = "string", example = "미미 정말 귀여워요!")
    @NotNull(message = "{validation.NotNull}")
    private String content;
}
