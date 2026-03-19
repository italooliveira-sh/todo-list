package com.italooliveira.projeto.todo_list.config;

import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;

@Configuration
@OpenAPIDefinition(
    info = @Info(
        title = "To-Do Pro API",
        version = "v1",
        description = "API para gerenciamento de tarefas com autenticação.",
        contact = @Contact(
            name = "Italo Oliveira",
            url = "https://github.com/ItaloOliveira-dev",
            email = "italooliveiraxdd@gmail.com"
        ),
        license = @License(
            name = "Apache 2.0",
            url = "https://www.apache.org/licenses/LICENSE-2.0"
        )
    ),
    servers = {
        @Server(url = "http://localhost:8080", description = "Servidor Local de Desenvolvimento")
    }
)
public class OpenApiConfig {
    
}
