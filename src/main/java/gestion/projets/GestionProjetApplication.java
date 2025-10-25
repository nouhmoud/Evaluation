package gestion.projets;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.Contact;

/**
 * Classe principale de l'application Spring Boot
 * Point d'entrée de l'application de gestion de projets
 */
@SpringBootApplication
@OpenAPIDefinition(
        info = @Info(
                title = "API de Gestion de Projets",
                version = "1.0",
                description = "API REST pour la gestion de projets, tâches et employés",
                contact = @Contact(
                        name = "Équipe de développement",
                        email = "contact@gestion-projets.com"
                )
        )
)
public class GestionProjetApplication {

    public static void main(String[] args) {
        SpringApplication.run(GestionProjetApplication.class, args);
        System.out.println("\n=================================================");
        System.out.println("✅ Application démarrée avec succès!");
        System.out.println("📋 API Docs JSON: http://localhost:8080/api-docs");
        System.out.println("=================================================\n");
    }
}