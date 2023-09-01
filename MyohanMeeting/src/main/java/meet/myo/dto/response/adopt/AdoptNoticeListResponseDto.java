package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(name = "AdoptNoticeList")
@Getter
public class AdoptNoticeListResponseDto {
    private Long totalRows;
    private Long totalPages;
    private List<AdoptNoticeSummaryResponseDto> adoptNoticeList;

    public static AdoptNoticeListResponseDto of(long totalRows, long totalPages, List<AdoptNoticeSummaryResponseDto> adoptNoticeList) {
        AdoptNoticeListResponseDto dto = new AdoptNoticeListResponseDto();
        dto.totalRows = totalRows;
        dto.totalPages = totalPages;
        dto.adoptNoticeList = adoptNoticeList;

        return dto;
    }
}
