package ma.sool.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                // allow all origins to access our service
                registry.addMapping("/**") // 전체 app에 대해 cors를 enable
                        .allowedOrigins("*")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "HEAD", "PATCH")
                        .allowedHeaders("*");
            }
        };
    }
}
/**
 fetch('http://localhost:8080/api/v1/users/login', {
     method: 'POST',
     headers: {
     Authorization: 'Basic ' + btoa('kim:123456')
     }
 })
     .then(response => response.json())
     .then(data => console.log('Success:', data))
     .catch(error => console.log(error))
 */