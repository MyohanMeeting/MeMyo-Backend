package meet.myo.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;


// https://springdoc.org/v2/#features
// https://happy-jjang-a.tistory.com/165
// https://velog.io/@jeong-god/Spring-boot-Swagger-API-%EC%97%B0%EB%8F%99%ED%95%98%EA%B8%B0
// https://devbksheen.tistory.com/entry/Spring-Docs-Swagger-%EC%84%A4%EC%A0%95%ED%95%98%EA%B8%B0-Spring-Rest-Docs-%EB%B9%84%EA%B5%90
//https://tg360.tistory.com/entry/Springdoc-openapi%EB%A5%BC-%ED%99%9C%EC%9A%A9%ED%95%9C-Spring-Boot-%EA%B8%B0%EB%B0%98-API%EC%9D%98-%EB%AC%B8%EC%84%9C-%EC%9E%90%EB%8F%99%ED%99%94

@OpenAPIDefinition(
        info = @Info(
                title = "MyoHanMeeting Backend",
                description = "유기묘 입양 서비스 묘한만남의 백엔드 서버 문서입니다.",
                version = "v1",
                contact = @Contact(
                        email = "bomlee427@gmail.com"
                )
        ),
        tags = {
                @Tag(name = "0. Authentication", description = "인증 관련 기능"),
                @Tag(name = "1. Sign Up", description = "회원가입 관련 기능"),
                @Tag(name = "2. Member", description = "회원 관련 기능"),
                @Tag(name = "3. Adopt Notice", description = "분양공고 관련 기능"),
                @Tag(name = "4. Adopt Application", description = "분양신청 관련 기능"),
                @Tag(name = "5. Adopt Notice Comment", description = "분양공고 댓글 관련 기능"),
                @Tag(name = "6. Favorite", description = "최애친구 관련 기능"),
                @Tag(name = "7. File", description = "파일 업로드 관련 기능")
        },
        servers = {
                @Server(url = "https://myomeet.store")
        }
)
@SecuritySchemes({
        @SecurityScheme(
                type = SecuritySchemeType.HTTP,
                scheme = "bearer",
                bearerFormat = "JWT",
                in = SecuritySchemeIn.HEADER,
                name = HttpHeaders.AUTHORIZATION),
        @SecurityScheme(type = SecuritySchemeType.DEFAULT)
})
@Configuration
public class SpringDocsConfig {
}
