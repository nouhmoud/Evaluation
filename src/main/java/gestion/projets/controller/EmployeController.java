package gestion.projets.controller;

import gestion.projets.model.Employe;
import gestion.projets.service.EmployeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour la gestion des employés
 * Expose les endpoints de l'API
 */
@RestController
@RequestMapping("/api/employes")
@Tag(name = "Employés", description = "API de gestion des employés")
@CrossOrigin(origins = "*")
public class EmployeController {

    @Autowired
    private EmployeService employeService;

    @GetMapping
    @Operation(summary = "Récupérer tous les employés",
            description = "Retourne la liste complète de tous les employés")
    @ApiResponse(responseCode = "200", description = "Liste des employés récupérée avec succès")
    public ResponseEntity<List<Employe>> getAllEmployes() {
        List<Employe> employes = employeService.getAllEmployes();
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/{matricule}")
    @Operation(summary = "Récupérer un employé par matricule",
            description = "Retourne les détails d'un employé spécifique")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> getEmployeByMatricule(
            @Parameter(description = "Matricule de l'employé", required = true, example = "EMP001")
            @PathVariable String matricule) {
        return employeService.getEmployeByMatricule(matricule)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/email/{email}")
    @Operation(summary = "Récupérer un employé par email",
            description = "Retourne les détails d'un employé à partir de son email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé trouvé"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Employe> getEmployeByEmail(
            @Parameter(description = "Email de l'employé", required = true, example = "jean.dupont@entreprise.com")
            @PathVariable String email) {
        return employeService.getEmployeByEmail(email)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Créer un nouvel employé",
            description = "Crée un nouveau employé dans le système")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Employé créé avec succès"),
            @ApiResponse(responseCode = "400", description = "Données invalides ou employé déjà existant")
    })
    public ResponseEntity<Employe> createEmploye(
            @Parameter(description = "Données de l'employé à créer", required = true)
            @Valid @RequestBody Employe employe) {
        try {
            Employe nouvelEmploye = employeService.createEmploye(employe);
            return ResponseEntity.status(HttpStatus.CREATED).body(nouvelEmploye);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{matricule}")
    @Operation(summary = "Mettre à jour un employé",
            description = "Met à jour les informations d'un employé existant")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Employé mis à jour avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable"),
            @ApiResponse(responseCode = "400", description = "Données invalides")
    })
    public ResponseEntity<Employe> updateEmploye(
            @Parameter(description = "Matricule de l'employé", required = true, example = "EMP001")
            @PathVariable String matricule,
            @Parameter(description = "Nouvelles données de l'employé", required = true)
            @Valid @RequestBody Employe employeDetails) {
        try {
            Employe employeMisAJour = employeService.updateEmploye(matricule, employeDetails);
            return ResponseEntity.ok(employeMisAJour);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{matricule}")
    @Operation(summary = "Supprimer un employé",
            description = "Supprime un employé du système (ainsi que toutes ses affectations)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Employé supprimé avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Void> deleteEmploye(
            @Parameter(description = "Matricule de l'employé", required = true, example = "EMP001")
            @PathVariable String matricule) {
        try {
            employeService.deleteEmploye(matricule);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/search")
    @Operation(summary = "Rechercher des employés",
            description = "Recherche des employés dont le nom ou le prénom contient le terme spécifié")
    @ApiResponse(responseCode = "200", description = "Recherche effectuée avec succès")
    public ResponseEntity<List<Employe>> searchEmployes(
            @Parameter(description = "Terme de recherche", required = true, example = "Dupont")
            @RequestParam String terme) {
        List<Employe> employes = employeService.searchEmployes(terme);
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/tache/{idTache}")
    @Operation(summary = "Récupérer les employés d'une tâche",
            description = "Retourne tous les employés assignés à une tâche spécifique")
    @ApiResponse(responseCode = "200", description = "Employés récupérés avec succès")
    public ResponseEntity<List<Employe>> getEmployesByTache(
            @Parameter(description = "ID de la tâche", required = true, example = "1")
            @PathVariable Integer idTache) {
        List<Employe> employes = employeService.getEmployesByTache(idTache);
        return ResponseEntity.ok(employes);
    }

    @GetMapping("/{matricule}/taches/count")
    @Operation(summary = "Compter les tâches d'un employé",
            description = "Retourne le nombre de tâches assignées à un employé")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Comptage effectué avec succès"),
            @ApiResponse(responseCode = "404", description = "Employé introuvable")
    })
    public ResponseEntity<Long> countTaches(
            @Parameter(description = "Matricule de l'employé", required = true, example = "EMP001")
            @PathVariable String matricule) {
        if (!employeService.getEmployeByMatricule(matricule).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        Long count = employeService.countTachesByEmploye(matricule);
        return ResponseEntity.ok(count);
    }
}