package meet.myo.dto.schema;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;

import java.util.List;

@Schema(name = "cat")
@Getter
public class CatResponseSchema {

    private Long catId;
    private String name;
    private String age;
    private String registered;
    private String registNumber;
    private String species;
    private String sex;
    private Double weight;
    private String neutered;
    private List<UploadSummarySchema> pictures;
    private String healthStatus;
    private String personality;
    private String foundedPlace;
    private String foundedAt;
    private ShelterSchema shelter;
    private String createdAt;

}