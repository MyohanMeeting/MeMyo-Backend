package meet.myo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private String clientUrl;

    public WebConfig(@Value("${client-env.url}") String clientUrl) {
        this.clientUrl = clientUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(clientUrl)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE");
    }
}
