package meet.myo.dto.request;

import lombok.Getter;

import java.util.List;

@Getter
public class DeleteFilesRequestDto {
    private List<Long> uploadIdList;
}
