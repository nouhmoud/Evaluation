package gestion.projets.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * Configuration de Swagger/OpenAPI
 * Personnalise la documentation de l'API
 */
@Configuration
public class SwaggerConfig {

    /**
     * Configuration personnalisée de l'OpenAPI
     * @return Instance OpenAPI configurée
     */
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API de Gestion de Projets")
                        .version("1.0.0")
                        .description("Documentation complète de l'API REST pour la gestion de projets, tâches et employés. " +
                                "Cette API permet de créer, lire, mettre à jour et supprimer des projets et leurs tâches associées.")
                        .contact(new Contact()
                                .name("Support Technique")
                                .email("support@gestion-projets.com")
                                .url("https://www.gestion-projets.com"))
                        .license(new License()
                                .name("Apache 2.0")
                                .url("https://www.apache.org/licenses/LICENSE-2.0.html")))
                .servers(List.of(
                        new Server()
                                .url("http://localhost:8080")
                                .description("Serveur de développement local"),
                        new Server()
                                .url("https://api.gestion-projets.com")
                                .description("Serveur de production")
                ));
    }
}