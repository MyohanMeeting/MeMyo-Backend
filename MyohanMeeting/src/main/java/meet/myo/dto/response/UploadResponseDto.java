package meet.myo.dto.response;

import meet.myo.domain.Upload;

public class UploadResponseDto {
    private Long id;
    private String url;
    private String originName;
    private String savedName;
    private String type;
    private String extension;
    private Long size;

    public static UploadResponseDto fromEntity(Upload upload) {
        UploadResponseDto dto = new UploadResponseDto();
        dto.id = upload.getId();
        dto.url = upload.getUrl();
        dto.originName = upload.getOriginName();
        dto.savedName = upload.getSavedName();
        dto.type = upload.getType();
        dto.extension = upload.getExtension();
        dto.size = upload.getSize();
        return dto;
    }
}
