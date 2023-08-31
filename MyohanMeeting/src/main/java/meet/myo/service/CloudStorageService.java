package meet.myo.service;

import com.google.cloud.storage.BlobId;
import com.google.cloud.storage.BlobInfo;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class CloudStorageService {

    private final String projectId;

    private final String bucketName;

    public CloudStorageService(
            @Value("${spring.cloud.gcp.storage.project-id}") String projectId,
            @Value("${spring.cloud.gcp.storage.bucket}") String bucketName
    ) {
        this.projectId = projectId;
        this.bucketName = bucketName;
    }

    /**
     * 버킷에 파일 업로드
     */
    public void doUpload(MultipartFile file, String objectName) throws IOException {
        Storage storage = StorageOptions.newBuilder().setProjectId(this.projectId).build().getService();
        BlobId blobId = BlobId.of(this.bucketName, objectName);
        BlobInfo blobInfo = BlobInfo.newBuilder(blobId)
                .setContentType(file.getContentType())
                .build();

        storage.create(blobInfo, file.getBytes());
    }

    /**
     * 버킷에서 파일 삭제
     */
    public void doDelete(String url) {
        String objectName = getObjectName(url);
        Storage storage = StorageOptions.newBuilder().setProjectId(this.projectId).build().getService();
        if (storage.get(bucketName, objectName) == null) {
            throw new IllegalArgumentException("URL에 해당하는 파일을 찾을 수 없습니다.");
        }

        storage.delete(bucketName, objectName);
    }

    /**
     * 버킷 객체명으로 파일 URL 생성
     */
    public String getUrl(String filePath) {
        return "https://storage.googleapis.com/" + this.bucketName + "/" + filePath;
    }

    /**
     * 파일 URL로부터 버킷 객체명 추출
     */
    private String getObjectName(String url) {
        return url.split(this.bucketName + "/")[1];
    }
}
