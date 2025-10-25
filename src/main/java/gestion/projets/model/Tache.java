package gestion.projets.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

/**
 * Entité représentant une Tâche
 * Correspond à la table "Tache" dans la base de données
 */
@Entity
@Table(name = "Tache")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Représente une tâche dans un projet")
public class Tache {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tache")
    @Schema(description = "Identifiant unique de la tâche", example = "1")
    private Integer idTache;

    @NotBlank(message = "Le titre de la tâche est obligatoire")
    @Column(name = "titre", nullable = false, length = 100)
    @Schema(description = "Titre de la tâche", example = "Développer le module de login", required = true)
    private String titre;

    @Column(name = "description", columnDefinition = "TEXT")
    @Schema(description = "Description détaillée de la tâche")
    private String description;

    @NotNull(message = "La date de début est obligatoire")
    @Column(name = "date_debut", nullable = false)
    @Schema(description = "Date de début de la tâche", example = "2025-01-15", required = true)
    private LocalDate dateDebut;

    @Column(name = "date_fin")
    @Schema(description = "Date de fin prévue de la tâche", example = "2025-02-15")
    private LocalDate dateFin;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut")
    @Schema(description = "Statut de la tâche", example = "EN_COURS", allowableValues = {"EN_ATTENTE", "EN_COURS", "TERMINEE"})
    private StatutTache statut = StatutTache.EN_ATTENTE;

    /**
     * Relation Many-to-One avec Projet
     * Plusieurs tâches appartiennent à un projet
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_projet", nullable = false)
    @JsonBackReference
    @Schema(description = "Projet auquel appartient la tâche")
    private Projet projet;

    /**
     * Relation Many-to-Many avec Employe via EmployeeTache
     * Une tâche peut être assignée à plusieurs employés
     */
    @OneToMany(mappedBy = "tache", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    @Schema(description = "Employés assignés à cette tâche")
    private Set<EmployeeTache> affectations = new HashSet<>();

    /**
     * Énumération des statuts possibles d'une tâche
     */
    public enum StatutTache {
        EN_ATTENTE("En attente"),
        EN_COURS("En cours"),
        TERMINEE("Terminée");

        private final String libelle;

        StatutTache(String libelle) {
            this.libelle = libelle;
        }

        public String getLibelle() {
            return libelle;
        }
    }
}