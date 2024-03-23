package com.example.immo.configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;

// http://127.0.0.1:3001/swagger-ui/index.html

@OpenAPIDefinition(
        info = @Info(
            title = "OpenApi Specifications - Chatop",
            version = "1.0",
            description = "Chatop Rest API Documentation",
            license = @License(name = "Version 1.0", url = "http://127.0.0.1:3001/"),
            contact = @Contact( name = "askldd", email = "askldd@askldd.com")
        ),
        servers = @Server(
            description = "local ENV",
            url = "http://127.0.0.1:3001/"
        )
)
@SecurityScheme(
        name = "bearerAuth",
        description = "JWT Auth Description",
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
public class OpenApiConfig {

}