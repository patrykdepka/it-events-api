package pl.patrykdepka.iteventsapi.core;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI itEventsOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("it-events-api")
                        .version("1.0.0")
                        .description("This is an API for IT events management.")
                )
                .components(new Components()
                        .addSecuritySchemes(
                                "JWT Token",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .name("Authorization")
                                        .in(SecurityScheme.In.HEADER)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")
                        )
                )
                .addSecurityItem(new SecurityRequirement().addList("JWT Token", Arrays.asList("read", "write")));
    }
}
