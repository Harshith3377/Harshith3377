package com.harshith.userprofile.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI userProfileStoreOpenApi() {
        return new OpenAPI()
                .info(new Info()
                        .title("User Profile Store API")
                        .version("v1")
                        .description("API contract for a user profile key-value store."));
    }
}
