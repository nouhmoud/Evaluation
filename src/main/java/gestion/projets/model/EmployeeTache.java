package gestion.projets.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import io.swagger.v3.oas.annotations.media.Schema;

import java.io.Serializable;

/**
 * Entité représentant la relation Many-to-Many entre Employé et Tâche
 * Table de liaison "EmployeeTache" avec clé composite
 */
@Entity
@Table(name = "EmployeeTache")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente l'affectation d'un employé à une tâche")
public class EmployeeTache {

    @EmbeddedId
    private EmployeeTacheId id = new EmployeeTacheId();

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("matricule")
    @JoinColumn(name = "matricule")
    @Schema(description = "Employé affecté")
    private Employe employe;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("idTache")
    @JoinColumn(name = "id_tache")
    @Schema(description = "Tâche assignée")
    private Tache tache;

    @Column(name = "role", length = 50)
    @Schema(description = "Rôle de l'employé sur cette tâche", example = "Développeur")
    private String role;

    /**
     * Constructeur avec paramètres
     */
    public EmployeeTache(Employe employe, Tache tache, String role) {
        this.employe = employe;
        this.tache = tache;
        this.role = role;
        this.id = new EmployeeTacheId(employe.getMatricule(), tache.getIdTache());
    }

    /**
     * Classe interne représentant la clé composite
     * pour la table de liaison EmployeeTache
     */
    @Embeddable
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class EmployeeTacheId implements Serializable {

        @Column(name = "matricule")
        private String matricule;

        @Column(name = "id_tache")
        private Integer idTache;
    }
}