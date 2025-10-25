package gestion.projets.service;

import gestion.projets.model.Projet;
import gestion.projets.repository.ProjetRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des projets
 * Contient la logique métier
 */
@Service
@Transactional
public class ProjetService {

    @Autowired
    private ProjetRepository projetRepository;

    /**
     * Récupère tous les projets
     * @return Liste de tous les projets
     */
    public List<Projet> getAllProjets() {
        return projetRepository.findAll();
    }

    /**
     * Récupère un projet par son ID
     * @param id ID du projet
     * @return Optional contenant le projet si trouvé
     */
    public Optional<Projet> getProjetById(Integer id) {
        return projetRepository.findById(id);
    }

    /**
     * Crée un nouveau projet
     * @param projet Projet à créer
     * @return Projet créé avec son ID généré
     */
    public Projet createProjet(Projet projet) {
        // Validation métier
        if (projet.getDateFin() != null && projet.getDateFin().isBefore(projet.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }
        return projetRepository.save(projet);
    }

    /**
     * Met à jour un projet existant
     * @param id ID du projet à modifier
     * @param projetDetails Nouvelles données du projet
     * @return Projet mis à jour
     */
    public Projet updateProjet(Integer id, Projet projetDetails) {
        Projet projet = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Projet introuvable avec l'ID: " + id));

        // Validation métier
        if (projetDetails.getDateFin() != null &&
                projetDetails.getDateFin().isBefore(projetDetails.getDateDebut())) {
            throw new IllegalArgumentException("La date de fin ne peut pas être antérieure à la date de début");
        }

        projet.setNom(projetDetails.getNom());
        projet.setDescription(projetDetails.getDescription());
        projet.setDateDebut(projetDetails.getDateDebut());
        projet.setDateFin(projetDetails.getDateFin());

        return projetRepository.save(projet);
    }

    /**
     * Supprime un projet
     * @param id ID du projet à supprimer
     */
    public void deleteProjet(Integer id) {
        if (!projetRepository.existsById(id)) {
            throw new RuntimeException("Projet introuvable avec l'ID: " + id);
        }
        projetRepository.deleteById(id);
    }

    /**
     * Recherche des projets par nom
     * @param nom Nom ou partie du nom
     * @return Liste des projets correspondants
     */
    public List<Projet> searchProjetsByNom(String nom) {
        return projetRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Récupère les projets actifs
     * @return Liste des projets actifs
     */
    public List<Projet> getProjetsActifs() {
        return projetRepository.findProjetsActifs(LocalDate.now());
    }

    /**
     * Récupère les projets dans une période donnée
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des projets
     */
    public List<Projet> getProjetsByPeriode(LocalDate dateDebut, LocalDate dateFin) {
        return projetRepository.findByDateDebutBetween(dateDebut, dateFin);
    }

    /**
     * Compte le nombre de tâches d'un projet
     * @param idProjet ID du projet
     * @return Nombre de tâches
     */
    public Long countTachesByProjet(Integer idProjet) {
        return projetRepository.countTachesByProjetId(idProjet);
    }
}