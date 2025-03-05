package com.skoy.bootcamp_microservices.config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestión de transacciones")
                        .description("API para el bootcamp de microservicios")
                        .version("1.0.0")
                        .contact(new Contact()
                                .name("Soporte")
                                .email("soporte@skoy.pe")
                                .url("https://www.skoy.pe"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://springdoc.org")))
                .externalDocs(new ExternalDocumentation()
                        .description("Documentación del Proyecto")
                        .url("https://github.com/Coyaks/microservice_transaction_service.git"));
    }
}
