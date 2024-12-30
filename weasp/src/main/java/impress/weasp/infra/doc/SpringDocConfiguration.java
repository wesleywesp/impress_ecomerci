package impress.weasp.infra.doc;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SpringDocConfiguration {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components()
                        .addSecuritySchemes("bearer-key",
                                new SecurityScheme()
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")))
                .info(new Info()
                        .title("E-Commerce API")
                        .description("API Rest da aplicação E-Commerce, com funcionalidades de cadastro de usuários, produtos, categorias e carrinho de compras.")
                        .contact(new Contact()
                                .name("Time Backend")
                                .email("WesleyWesp@gmail.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("http://WespCompanim/api/licenca")));
    }
}
