package gestion.projets.service;

import gestion.projets.model.Tache;
import gestion.projets.model.Tache.StatutTache;
import gestion.projets.model.Projet;
import gestion.projets.model.Employe;
import gestion.projets.model.EmployeeTache;
import gestion.projets.repository.TacheRepository;
import gestion.projets.repository.ProjetRepository;
import gestion.projets.repository.EmployeRepository;
import gestion.projets.repository.EmployeeTacheRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des tâches
 * Contient la logique métier
 */
@Service
@Transactional
public class TacheService {

    @Autowired
    private TacheRepository tacheRepository;

    @Autowired
    private ProjetRepository projetRepository;

    @Autowired
    private EmployeRepository employeRepository;

    @Autowired
    private EmployeeTacheRepository employeeTacheRepository;

    /**
     * Récupère toutes les tâches
     * @return Liste de toutes les tâches
     */
    public List<Tache> getAllTaches() {
        return tacheRepository.findAll();
    }

    /**
     * Récupère une tâche par son ID
     * @param id ID de la tâche
     * @return Optional contenant la tâche si trouvée
     */
    public Optional<Tache> getTacheById(Integer id) {
        return tacheRepository.findById(id);
    }

    /**
     * Récupère toutes les tâches d'un projet
     * @param idProjet ID du projet
     * @return Liste des tâches du projet
     */
    public List<Tache> getTachesByProjet(Integer idProjet) {
        return tacheRepository.findByProjetIdProjet(idProjet);
    }

    /**
     * Récupère les tâches par statut
     * @param statut Statut recherché
     * @return Liste des tâches
     */
    public List<Tache> getTachesByStatut(StatutTache statut) {
        return tacheRepository.findByStatut(statut);
    }

    /**
     * Crée une nouvelle tâche
     * @param tache Tâche à créer
     * @param idProjet ID du projet auquel rattacher la tâche
     * @return Tâche créée
     */
    public Tache createTache(Tache tache, Integer idProjet) {
        Projet projet = projetRepository.findById(idProjet)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec l'ID: " + idProjet));

        // Validation métier
        if (tache.getDateFin() != null && tache.getDateFin().isBefore(tache.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        tache.setProjet(projet);
        return tacheRepository.save(tache);
    }

    /**
     * Met à jour une tâche existante
     * @param id ID de la tâche à modifier
     * @param tacheDetails Nouvelles données de la tâche
     * @return Tâche mise à jour
     */
    public Tache updateTache(Integer id, Tache tacheDetails) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable avec l'ID: " + id));

        // Validation métier
        if (tacheDetails.getDateFin() != null &&
                tacheDetails.getDateFin().isBefore(tacheDetails.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        tache.setTitre(tacheDetails.getTitre());
        tache.setDescription(tacheDetails.getDescription());
        tache.setDateDebut(tacheDetails.getDateDebut());
        tache.setDateFin(tacheDetails.getDateFin());
        tache.setStatut(tacheDetails.getStatut());

        return tacheRepository.save(tache);
    }

    /**
     * Change le statut d'une tâche
     * @param id ID de la tâche
     * @param statut Nouveau statut
     * @return Tâche mise à jour
     */
    public Tache changeStatut(Integer id, StatutTache statut) {
        Tache tache = tacheRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable avec l'ID: " + id));

        tache.setStatut(statut);
        return tacheRepository.save(tache);
    }

    /**
     * Supprime une tâche
     * @param id ID de la tâche à supprimer
     */
    public void deleteTache(Integer id) {
        if (!tacheRepository.existsById(id)) {
            throw new RuntimeException("Tâche introuvable avec l'ID: " + id);
        }
        tacheRepository.deleteById(id);
    }

    /**
     * Assigne un employé à une tâche
     * @param idTache ID de la tâche
     * @param matricule Matricule de l'employé
     * @param role Rôle de l'employé sur la tâche
     * @return EmployeeTache créé
     */
    public EmployeeTache assignerEmploye(Integer idTache, String matricule, String role) {
        Tache tache = tacheRepository.findById(idTache)
                .orElseThrow(() -> new RuntimeException("Tâche introuvable avec l'ID: " + idTache));

        Employe employe = employeRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Employé introuvable avec le matricule: " + matricule));

        // Vérifier si l'affectation existe déjà
        if (employeeTacheRepository.existsByEmployeMatriculeAndTacheIdTache(matricule, idTache)) {
            throw new IllegalArgumentException("Cet employé est déjà assigné à cette tâche");
        }

        EmployeeTache affectation = new EmployeeTache(employe, tache, role);
        return employeeTacheRepository.save(affectation);
    }

    /**
     * Retire un employé d'une tâche
     * @param idTache ID de la tâche
     * @param matricule Matricule de l'employé
     */
    public void retirerEmploye(Integer idTache, String matricule) {
        EmployeeTache.EmployeeTacheId id = new EmployeeTache.EmployeeTacheId(matricule, idTache);

        if (!employeeTacheRepository.existsById(id)) {
            throw new RuntimeException("Affectation introuvable");
        }

        employeeTacheRepository.deleteById(id);
    }

    /**
     * Récupère les tâches en retard
     * @return Liste des tâches en retard
     */
    public List<Tache> getTachesEnRetard() {
        return tacheRepository.findTachesEnRetard(LocalDate.now());
    }

    /**
     * Récupère les tâches d'un employé
     * @param matricule Matricule de l'employé
     * @return Liste des tâches
     */
    public List<Tache> getTachesByEmploye(String matricule) {
        return tacheRepository.findTachesByEmployeMatricule(matricule);
    }

    /**
     * Recherche des tâches par titre
     * @param titre Titre ou partie du titre
     * @return Liste des tâches correspondantes
     */
    public List<Tache> searchTachesByTitre(String titre) {
        return tacheRepository.findByTitreContainingIgnoreCase(titre);
    }
}