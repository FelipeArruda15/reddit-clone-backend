package com.felipearruda.redditclone.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(servers = {@Server(url = "http://localhost:8080", description = "URL padrão do servidor")})
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        scheme = "bearer"
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI getApiInfo() {
        return new OpenAPI().components(new Components()).info(new Info().title("API REST: Reddit Clone").version("v1.0.0").description("API para o Reddit Clone").contact(new Contact().name("Felipe Arruda").email("souzafelipesoua5@gmail.com")));
    }

    @Bean
    public OpenApiCustomizer customerResponses() {
        return openApi -> openApi.getPaths().values().forEach(pathItem -> pathItem.readOperations().forEach(operation -> {

            ApiResponses apiResponses = operation.getResponses();

            apiResponses.addApiResponse("200", createApiResponse("Sucesso!"));
            apiResponses.addApiResponse("201", createApiResponse("Criado com Sucesso!"));
            apiResponses.addApiResponse("204", createApiResponse("Não contem!"));
            apiResponses.addApiResponse("400", createApiResponse("Erro na Requisição!"));
            apiResponses.addApiResponse("401", createApiResponse("Acesso Não Autorizado!"));
            apiResponses.addApiResponse("404", createApiResponse("Não encontramos nada!"));
            apiResponses.addApiResponse("500", createApiResponse("Erro na Aplicação!"));
        }));
    }

    private ApiResponse createApiResponse(String message) {
        return new ApiResponse().description(message);
    }

}

