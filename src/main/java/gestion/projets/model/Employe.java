package gestion.projets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant un Employé
 * Correspond à la table "Employe" dans la base de données
 */
@Entity
@Table(name = "Employe")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un employé dans le système")
public class Employe {

    @Id
    @Column(name = "matricule", length = 20)
    @NotBlank(message = "Le matricule est obligatoire")
    @Schema(description = "Matricule unique de l'employé", example = "EMP001", required = true)
    private String matricule;

    @NotBlank(message = "Le nom est obligatoire")
    @Column(name = "nom", nullable = false, length = 50)
    @Schema(description = "Nom de famille de l'employé", example = "Dupont", required = true)
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    @Column(name = "prenom", nullable = false, length = 50)
    @Schema(description = "Prénom de l'employé", example = "Jean", required = true)
    private String prenom;

    @Email(message = "L'email doit être valide")
    @NotBlank(message = "L'email est obligatoire")
    @Column(name = "email", nullable = false, unique = true, length = 100)
    @Schema(description = "Adresse email professionnelle", example = "jean.dupont@entreprise.com", required = true)
    private String email;

    /**
     * Relation Many-to-Many avec Tache via EmployeeTache
     * Un employé peut être assigné à plusieurs tâches
     */
    @OneToMany(mappedBy = "employe", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Affectations de l'employé aux tâches")
    private Set<EmployeeTache> affectations = new HashSet<>();

    /**
     * Constructeur avec les champs principaux
     */
    public Employe(String matricule, String nom, String prenom, String email) {
        this.matricule = matricule;
        this.nom = nom;
        this.prenom = prenom;
        this.email = email;
    }
}