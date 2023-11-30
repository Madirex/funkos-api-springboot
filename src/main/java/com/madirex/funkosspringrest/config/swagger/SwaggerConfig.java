package com.madirex.funkosspringrest.config.swagger;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Swagger Configuration
 */
@Configuration
class SwaggerConfig {

    /**
     * API Key Scheme
     *
     * @return SecurityScheme
     */
    private SecurityScheme createAPIKeyScheme() {
        return new SecurityScheme().type(SecurityScheme.Type.HTTP)
                .bearerFormat("JWT")
                .scheme("bearer");
    }

    /**
     * OpenAPI Info Configuration
     *
     * @return OpenAPI
     */
    @Bean
    OpenAPI apiInfo() {
        return new OpenAPI()
                .info(
                        new Info()
                                .title("Funkos API Rest")
                                .version("1.0.0")
                                .description("API REST de Funkos")
                                .termsOfService("https://www.madirex.com/p/license.html")
                                .license(
                                        new License()
                                                .name("CC BY-NC-SA 4.0")
                                                .url("https://www.madirex.com/p/license.html")
                                )
                                .contact(
                                        new Contact()
                                                .name("Madirex")
                                                .email("contact@madirex.com")
                                                .url("https://www.madirex.com/")
                                )

                )
                .addSecurityItem(new SecurityRequirement().
                        addList("Bearer Authentication"))
                .components(new Components().addSecuritySchemes
                        ("Bearer Authentication", createAPIKeyScheme()));
    }


    /**
     * Swagger UI
     *
     * @return GroupedOpenApi
     */
    @Bean
    GroupedOpenApi httpApi() {
        return GroupedOpenApi.builder()
                .group("https")
                .pathsToMatch("/api/funkos/**")
                .displayName("Funkos")
                .build();
    }
}