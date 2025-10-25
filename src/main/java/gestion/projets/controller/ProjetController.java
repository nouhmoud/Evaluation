package gestion.projets.controller;

import gestion.projets.model.Projet;
import gestion.projets.service.ProjetService;
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
 * Contrôleur REST pour la gestion des projets
 * Expose les endpoints de l'API
 */
@RestController
@RequestMapping("/api/projets")
@Tag(name = "Projets", description = "API de gestion des projets")
@CrossOrigin(origins = "*")
public class ProjetController {

    @Autowired
    private ProjetService projetService;

    @GetMapping
    @Operation(summary = "Récupérer tous les projets",
            description = "Retourne la liste complète de tous les projets")
    @ApiResponse(responseCode = "200", description = "Liste des projets récupérée avec succès")
    public ResponseEntity<List<Projet>> getAllProjets() {
        List<Projet> projets = projetService.getAllProjets();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Récupérer un projet par ID",
            description = "Retourne les détails d'un projet spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet trouvé"),
            @ApiResponse(responseCode = "404", description = "Projet introuvable")
    })
    public ResponseEntity<Projet> getProjetById(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable Integer id) {
        return projetService.getProjetById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau projet",
            description = "Crée un nouveau projet dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Projet créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Projet> createProjet(
            @Parameter(description = "Données du projet à créer", required = true)
            @Valid @RequestBody Projet projet) {
        try {
            Projet nouveauProjet = projetService.createProjet(projet);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouveauProjet);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @Operation(summary = "Mettre à jour un projet",
            description = "Met à jour les informations d'un projet existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Projet mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Projet introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Projet> updateProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable Integer id,
            @Parameter(description = "Nouvelles données du projet", required = true)
            @Valid @RequestBody Projet projetDetails) {
        try {
            Projet projetMisAJour = projetService.updateProjet(id, projetDetails);
            return ResponseEntity.ok(projetMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Supprimer un projet",
            description = "Supprime un projet du système (ainsi que toutes ses tâches)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Projet supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Projet introuvable")
    })
    public ResponseEntity<Void> deleteProjet(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable Integer id) {
        try {
            projetService.deleteProjet(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des projets par nom",
            description = "Recherche des projets dont le nom contient le terme spécifié")
    @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    public ResponseEntity<List<Projet>> searchProjets(
            @Parameter(description = "Terme de recherche", required = true, example = "Gestion")
            @RequestParam String nom) {
        List<Projet> projets = projetService.searchProjetsByNom(nom);
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/actifs")
    @Operation(summary = "Récupérer les projets actifs",
            description = "Retourne tous les projets dont la date de fin n'est pas dépassée")
    @ApiResponse(responseCode = "200", description = "Projets actifs récupérés avec succès")
    public ResponseEntity<List<Projet>> getProjetsActifs() {
        List<Projet> projets = projetService.getProjetsActifs();
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/periode")
    @Operation(summary = "Récupérer les projets d'une période",
            description = "Retourne les projets dont la date de début est dans la période spécifiée")
    @ApiResponse(responseCode = "200", description = "Projets récupérés avec succès")
    public ResponseEntity<List<Projet>> getProjetsByPeriode(
            @Parameter(description = "Date de début de la période", required = true, example = "2025-01-01")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateDebut,
            @Parameter(description = "Date de fin de la période", required = true, example = "2025-12-31")
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate dateFin) {
        List<Projet> projets = projetService.getProjetsByPeriode(dateDebut, dateFin);
        return ResponseEntity.ok(projets);
    }

    @GetMapping("/{id}/taches/count")
    @Operation(summary = "Compter les tâches d'un projet",
            description = "Retourne le nombre de tâches associées à un projet")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comptage effectué avec succès"),
            @ApiResponse(responseCode = "404", description = "Projet introuvable")
    })
    public ResponseEntity<Long> countTaches(
            @Parameter(description = "ID du projet", required = true, example = "1")
            @PathVariable Integer id) {
        if (!projetService.getProjetById(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Long count = projetService.countTachesByProjet(id);
        return ResponseEntity.ok(count);
    }
}