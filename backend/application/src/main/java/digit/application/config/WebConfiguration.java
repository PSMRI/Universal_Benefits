package digit.application.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
 
@Configuration
public class WebConfiguration implements WebMvcConfigurer {
 
    @Override
    public void addCorsMappings(CorsRegistry registry) {
registry.addMapping("/**")
        .allowedOrigins("https://dev-uba-provider.tekdinext.com", "http://localhost:5173") // No trailing slash on localhost
        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
        .allowedHeaders("Authorization", "Content-Type")
        .allowCredentials(false)  // Set to true if you want to send credentials like cookies
        .maxAge(3600);  // Cache the CORS preflight response for 1 hour
    }
}
