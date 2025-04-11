package com.bubusyaka.demo.configuration;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {
    @OpenAPIDefinition(
            info = @Info(
                    title = "Loyalty System Api",
                    description = "API системы лояльности",
                    version = "1.0.0",
                    contact = @Contact(
                            name = "Bubu",
                            email = "bubu@bubu.bu"
                    )
            )
    )
    public class OpenApiConfig {
//        @Tag(name = "Название контроллера", description = "Описание контроллера")
//        public class ControllerName {
//            // Контент контроллера
//        }
        // Конфигурация для Swagger
    }


}
