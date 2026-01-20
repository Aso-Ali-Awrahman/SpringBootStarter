package com.aso.springstarter.configurations;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
    info = @Info(title = "Spring Boot Starter", version = "v1")
    // security
)
public class SwaggerConfiguration {
}
