package org.ru.backend.config;

import io.swagger.v3.oas.models.*;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.media.Content;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.RequestBody;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        final String securitySchemeName = "bearerAuth";
        return new OpenAPI()
                .addSecurityItem(new SecurityRequirement().addList(securitySchemeName))
                .components(
                        new Components()
                                .addSecuritySchemes(securitySchemeName,
                                        new SecurityScheme()
                                                .name(securitySchemeName)
                                                .type(SecurityScheme.Type.HTTP)
                                                .scheme("bearer")
                                                .bearerFormat("JWT")
                                )
                )
                .info(new Info().title("Auth API").version("1.0"))
                .paths(new Paths()
                        .addPathItem("/api/login", new PathItem().post(
                                                new Operation()
                                                        .tags(Collections.singletonList("Authentication"))
                                                        .summary("User login")
                                                        .requestBody(new RequestBody()
                                                                .content(new Content()
                                                                        .addMediaType("application/json",
                                                                                new MediaType().schema(new Schema()
                                                                                        .type("object")
                                                                                        .addProperties("username",
                                                                                                new Schema().type("string"))
                                                                                        .addProperties("password",
                                                                                                new Schema().type("string").format("password"))
                                                                                )
                                                                        )
                                                                )
                                                        )
                                                        .responses(new ApiResponses()
                                                                .addApiResponse("200", new ApiResponse()
                                                                        .description("Successfully log in"))
                                                                .addApiResponse("401", new ApiResponse()
                                                                        .description("Invalid username or password"))
                                                        )
                                        ))
                                        .addPathItem("/api/logout", new PathItem().post(
                                                        new Operation()
                                                                .tags(Collections.singletonList("Authentication"))
                                                                .summary("User logout")
                                                                .responses(new ApiResponses()
                                                                        .addApiResponse("200", new ApiResponse()
                                                                                .description("Successfully logged out"))
                                                                        .addApiResponse("401", new ApiResponse()
                                                                                .description("Unauthorized"))
                                                                )
                                                )
                                        )
                                        .addPathItem("/api/refresh", new PathItem().post(
                                                        new Operation()
                                                                .tags(Collections.singletonList("Authentication"))
                                                                .summary("Refresh access token")
                                                                .responses(new ApiResponses()
                                                                        .addApiResponse("200", new ApiResponse()
                                                                                .description("Success. Returns new access token"))
                                                                        .addApiResponse("401", new ApiResponse()
                                                                                .description("Invalid refresh token"))
                                                                )
                                                )
                                        )
                        );
    }
}