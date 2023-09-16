package meet.myo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String serverUrl;
    private final String clientUrl;

    public WebConfig(
            @Value("${deploy.client.url}") String serverUrl,
            @Value("${deploy.server.url}") String clientUrl) {
        this.serverUrl = serverUrl;
        this.clientUrl = clientUrl;
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins(serverUrl, clientUrl)
                .allowedMethods("*")
                .allowCredentials(false)
                .maxAge(3000);
    }
}
