package meet.myo.dto.request;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;

import java.util.List;

@Getter
public class DeleteFilesRequestDto {
    @NotNull(message = "{validation.NotNull}")
    private List<Long> uploadIdList;
}
