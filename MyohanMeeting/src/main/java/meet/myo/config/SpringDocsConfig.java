package meet.myo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;

// https://springdoc.org/v2/#features
// https://happy-jjang-a.tistory.com/165
// https://velog.io/@jeong-god/Spring-boot-Swagger-API-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0
// https://devbksheen.tistory.com/entry/Spring-Docs-Swagger-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0-Spring-Rest-Docs-%EB%B9%84%EA%B5%90

@Configuration
public class SpringDocsConfig {
    @Bean
    public OpenAPI myoBackV1(@Value("${springdoc.ver}") String version) {

        Info info = new Info()
                .title("MyoHanMeeting Backend")
                .version(version)
                .description("유기묘 입양 서비스 묘한만남의 백엔드 서버 문서입니다.")
                .contact(new Contact()
                        .email("bomlee427@gmail.com"));

        SecurityScheme bearerAuth = new SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .in(SecurityScheme.In.HEADER)
                .name(HttpHeaders.AUTHORIZATION);

        return new OpenAPI()
                .info(info)
                .components(new Components().addSecuritySchemes("JWT", bearerAuth)
                        .addSecuritySchemes("", new SecurityScheme()));
    }
}
