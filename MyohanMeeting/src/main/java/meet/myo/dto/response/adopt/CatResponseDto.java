package meet.myo.dto.response.adopt;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import meet.myo.domain.adopt.notice.cat.Cat;

@Schema(name = "cat")
@Getter
public class CatResponseDto {

    private String name;
    private String age;
    private String registered;
    private String registNumber;
    private String species;
    private String sex;
    private Double weight;
    private String neutered;
    private String healthStatus;
    private String personality;
    private String foundedPlace;
    private String foundedAt;

    public static CatResponseDto fromEntity(Cat cat) {
        CatResponseDto schema = new CatResponseDto();
        schema.name = cat.getName();
        schema.age = cat.getAge();
        schema.registered = cat.getRegistered().toString();
        schema.registNumber = cat.getRegistNumber();
        schema.species = cat.getSpecies();
        schema.sex = cat.getSex().toString();
        schema.weight = cat.getWeight();
        schema.neutered = cat.getNeutered().toString();
        schema.healthStatus = cat.getHealthStatus();
        schema.personality = cat.getPersonality();
        schema.foundedPlace = cat.getFoundedPlace();
        schema.foundedAt = cat.getFoundedAt();
        return schema;
    }
}
