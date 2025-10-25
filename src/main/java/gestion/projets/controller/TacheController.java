package gestion.projets.controller;

import gestion.projets.model.Tache;
import gestion.projets.model.Tache.StatutTache;
import gestion.projets.model.EmployeeTache;
import gestion.projets.service.TacheService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * Contrôleur REST pour la gestion des tâches
 * Expose les endpoints de l'API
 */
@RestController
@RequestMapping("/api/taches")
@Tag(name = "Tâches", description = "API de gestion des tâches")
@CrossOrigin(origins = "*")
public class TacheController {

    @Autowired
    private TacheService tacheService;

    // 📘 Récupérer toutes les tâches
    @GetMapping
    @Operation(summary = "Récupérer toutes les tâches", description = "Retourne la liste complète de toutes les tâches")
    public ResponseEntity<List<Tache>> getAllTaches() {
        return ResponseEntity.ok(tacheService.getAllTaches());
    }

    // 📘 Récupérer une tâche par ID
    @GetMapping("/{id}")
    @Operation(summary = "Récupérer une tâche par ID", description = "Retourne les détails d'une tâche spécifique")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche trouvée"),
            @ApiResponse(responseCode = "404", description = "Tâche introuvable")
    })
    public ResponseEntity<Tache> getTacheById(@PathVariable Integer id) {
        return tacheService.getTacheById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 📘 Créer une tâche (rattachée à un projet)
    @PostMapping("/projet/{idProjet}")
    @Operation(summary = "Créer une nouvelle tâche", description = "Crée une nouvelle tâche liée à un projet")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Tâche créée avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou projet introuvable")
    })
    public ResponseEntity<?> createTache(
            @Parameter(description = "ID du projet", example = "1") @PathVariable Integer idProjet,
            @Valid @RequestBody Tache tache) {
        try {
            Tache nouvelleTache = tacheService.createTache(tache, idProjet);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelleTache);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //  Mettre à jour une tâche
    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour une tâche", description = "Met à jour les informations d'une tâche existante")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Tâche mise à jour"),
            @ApiResponse(responseCode = "404", description = "Tâche introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<?> updateTache(@PathVariable Integer id, @Valid @RequestBody Tache tacheDetails) {
        try {
            return ResponseEntity.ok(tacheService.updateTache(id, tacheDetails));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // Supprimer une tâche
    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer une tâche", description = "Supprime une tâche du système")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Tâche supprimée"),
            @ApiResponse(responseCode = "404", description = "Tâche introuvable")
    })
    public ResponseEntity<?> deleteTache(@PathVariable Integer id) {
        try {
            tacheService.deleteTache(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 📘 Rechercher des tâches par titre
    @GetMapping("/search")
    @Operation(summary = "Rechercher des tâches", description = "Recherche des tâches dont le titre contient le terme spécifié")
    public ResponseEntity<List<Tache>> searchTaches(@RequestParam String titre) {
        return ResponseEntity.ok(tacheService.searchTachesByTitre(titre));
    }

    // Récupérer les tâches d’un projet
    @GetMapping("/projet/{idProjet}")
    @Operation(summary = "Récupérer les tâches d’un projet", description = "Retourne toutes les tâches d’un projet spécifique")
    public ResponseEntity<List<Tache>> getTachesByProjet(@PathVariable Integer idProjet) {
        return ResponseEntity.ok(tacheService.getTachesByProjet(idProjet));
    }

    // 📘 Récupérer les tâches d’un employé
    @GetMapping("/employe/{matricule}")
    @Operation(summary = "Récupérer les tâches d’un employé", description = "Retourne toutes les tâches assignées à un employé")
    public ResponseEntity<List<Tache>> getTachesByEmploye(@PathVariable String matricule) {
        return ResponseEntity.ok(tacheService.getTachesByEmploye(matricule));
    }

    // 📘 Récupérer les tâches en retard
    @GetMapping("/en-retard")
    @Operation(summary = "Récupérer les tâches en retard", description = "Retourne les tâches dont la date de fin est dépassée et non terminées")
    public ResponseEntity<List<Tache>> getTachesEnRetard() {
        return ResponseEntity.ok(tacheService.getTachesEnRetard());
    }

    // 📘 Filtrer par statut
    @GetMapping("/statut/{statut}")
    @Operation(summary = "Récupérer les tâches par statut", description = "Retourne les tâches selon leur statut (EN_COURS, TERMINEE, EN_ATTENTE)")
    public ResponseEntity<List<Tache>> getTachesByStatut(@PathVariable StatutTache statut) {
        return ResponseEntity.ok(tacheService.getTachesByStatut(statut));
    }

    // 📘 Changer le statut d’une tâche
    @PutMapping("/{id}/statut")
    @Operation(summary = "Changer le statut d’une tâche", description = "Met à jour uniquement le statut d’une tâche")
    public ResponseEntity<?> changeStatut(
            @PathVariable Integer id,
            @RequestParam StatutTache statut) {
        try {
            return ResponseEntity.ok(tacheService.changeStatut(id, statut));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    // 📘 Assigner un employé à une tâche
    @PostMapping("/{idTache}/assigner")
    @Operation(summary = "Assigner un employé à une tâche", description = "Associe un employé à une tâche avec un rôle donné")
    public ResponseEntity<?> assignerEmploye(
            @PathVariable Integer idTache,
            @RequestParam String matricule,
            @RequestParam String role) {
        try {
            EmployeeTache affectation = tacheService.assignerEmploye(idTache, matricule, role);
            return ResponseEntity.status(HttpStatus.CREATED).body(affectation);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // 📘 Retirer un employé d’une tâche
    @DeleteMapping("/{idTache}/retirer")
    @Operation(summary = "Retirer un employé d’une tâche", description = "Supprime une affectation entre un employé et une tâche")
    public ResponseEntity<?> retirerEmploye(
            @PathVariable Integer idTache,
            @RequestParam String matricule) {
        try {
            tacheService.retirerEmploye(idTache, matricule);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
