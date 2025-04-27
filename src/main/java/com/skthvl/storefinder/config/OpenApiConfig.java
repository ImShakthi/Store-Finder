package com.skthvl.storefinder.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.servers.Server;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for OpenAPI/Swagger documentation.
 * Sets up API information, servers, security schemes and contact details.
 */
@Configuration
public class OpenApiConfig {

  @Value("${store-finder.openapi.dev-url}")
  private String devUrl;

  @Value("${store-finder.openapi.prod-url}")
  private String prodUrl;

  /**
   * Configures and provides the OpenAPI documentation bean.
   * Sets up development and production servers, contact information,
   * licensing, API information and JWT security requirements.
   *
   * @return Configured OpenAPI instance
   */
  @Bean
  public OpenAPI myOpenApi() {
    final Server devServer = new Server();
    devServer.setUrl(devUrl);
    devServer.setDescription("Server URL in Development environment");

    final Server prodServer = new Server();
    prodServer.setUrl(prodUrl);
    prodServer.setDescription("Server URL in Production environment");

    final Contact contact = new Contact();
    contact.setEmail("skthvl94@gmail.com");
    contact.setName("Sakthivel Balasubramaniam");
    contact.setUrl("https://www.imshakthi.github.io/");

    final License mitLicense =
        new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

    final Info info =
        new Info()
            .title("Store-Finder API")
            .version("1.0")
            .contact(contact)
            .description(
                "This API exposes endpoints to view store related details.")
            .termsOfService("https://www.imshakthi.github.io/terms")
            .license(mitLicense);

    final SecurityScheme securityScheme =
        new SecurityScheme()
            .name("Authorization")
            .type(SecurityScheme.Type.HTTP)
            .scheme("bearer")
            .bearerFormat("JWT")
            .in(SecurityScheme.In.HEADER)
            .description("Enter JWT token: Bearer <token>");

    return new OpenAPI()
        .info(info)
        .servers(List.of(devServer, prodServer))
        .addSecurityItem(new SecurityRequirement().addList("bearerAuth"))
        .components(new Components().addSecuritySchemes("bearerAuth", securityScheme));
  }
}
