package io.corbs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.WebFluxConfigurer;

@Configuration
@EnableWebFlux
@CrossOrigin
public class WebConfig implements WebFluxConfigurer {

    @Value("${todos.cors.allowed.origin}")
    private String origin;

    @Override
    public void addCorsMappings(CorsRegistry registry) {

        registry.addMapping("/**")
                .allowedOrigins("http://localhost:4040")
                .allowedMethods("*")
                .allowedHeaders("*")
                .allowCredentials(true);

    }
}
