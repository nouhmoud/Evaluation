package gestion.projets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Entité représentant un Projet
 * Correspond à la table "Projet" dans la base de données
 */
@Entity
@Table(name = "Projet")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente un projet dans le système de gestion")
public class Projet {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_projet")
    @Schema(description = "Identifiant unique du projet", example = "1")
    private Integer idProjet;

    @NotBlank(message = "Le nom du projet est obligatoire")
    @Column(name = "nom", nullable = false, length = 100)
    @Schema(description = "Nom du projet", example = "Gestion de stock", required = true)
    private String nom;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description détaillée du projet", example = "Système de gestion des stocks pour l'entreprise")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    @Schema(description = "Date de début du projet", example = "2025-01-01", required = true)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    @Schema(description = "Date de fin prévue du projet", example = "2025-06-30")
    private LocalDate dateFin;

    /**
     * Relation One-to-Many avec Tache
     * Un projet peut avoir plusieurs tâches
     * CascadeType.ALL : toutes les opérations se propagent aux tâches
     * orphanRemoval : supprime les tâches orphelines
     */
    @OneToMany(mappedBy = "projet", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    @Schema(description = "Liste des tâches associées au projet")
    private List<Tache> taches = new ArrayList<>();

    /**
     * Méthode utilitaire pour ajouter une tâche au projet
     * Maintient la cohérence bidirectionnelle
     */
    public void addTache(Tache tache) {
        taches.add(tache);
        tache.setProjet(this);
    }

    /**
     * Méthode utilitaire pour retirer une tâche du projet
     * Maintient la cohérence bidirectionnelle
     */
    public void removeTache(Tache tache) {
        taches.remove(tache);
        tache.setProjet(null);
    }
}