package gestion.projets.service;

import gestion.projets.model.Employe;
import gestion.projets.repository.EmployeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service pour la gestion des employés
 * Contient la logique métier
 */
@Service
@Transactional
public class EmployeService {

    @Autowired
    private EmployeRepository employeRepository;

    /**
     * Récupère tous les employés
     * @return Liste de tous les employés
     */
    public List<Employe> getAllEmployes() {
        return employeRepository.findAll();
    }

    /**
     * Récupère un employé par son matricule
     * @param matricule Matricule de l'employé
     * @return Optional contenant l'employé si trouvé
     */
    public Optional<Employe> getEmployeByMatricule(String matricule) {
        return employeRepository.findById(matricule);
    }

    /**
     * Récupère un employé par son email
     * @param email Email de l'employé
     * @return Optional contenant l'employé si trouvé
     */
    public Optional<Employe> getEmployeByEmail(String email) {
        return employeRepository.findByEmail(email);
    }

    /**
     * Crée un nouvel employé
     * @param employe Employé à créer
     * @return Employé créé
     */
    public Employe createEmploye(Employe employe) {
        // Vérification si le matricule existe déjà
        if (employeRepository.existsById(employe.getMatricule())) {
            throw new IllegalArgumentException("Un employé avec ce matricule existe déjà");
        }

        // Vérification si l'email existe déjà
        if (employeRepository.existsByEmail(employe.getEmail())) {
            throw new IllegalArgumentException("Un employé avec cet email existe déjà");
        }

        return employeRepository.save(employe);
    }

    /**
     * Met à jour un employé existant
     * @param matricule Matricule de l'employé à modifier
     * @param employeDetails Nouvelles données de l'employé
     * @return Employé mis à jour
     */
    public Employe updateEmploye(String matricule, Employe employeDetails) {
        Employe employe = employeRepository.findById(matricule)
                .orElseThrow(() -> new RuntimeException("Employé introuvable avec le matricule: " + matricule));

        // Vérifier si le nouvel email n'est pas déjà utilisé par un autre employé
        if (!employe.getEmail().equals(employeDetails.getEmail()) &&
                employeRepository.existsByEmail(employeDetails.getEmail())) {
            throw new IllegalArgumentException("Cet email est déjà utilisé par un autre employé");
        }

        employe.setNom(employeDetails.getNom());
        employe.setPrenom(employeDetails.getPrenom());
        employe.setEmail(employeDetails.getEmail());

        return employeRepository.save(employe);
    }

    /**
     * Supprime un employé
     * @param matricule Matricule de l'employé à supprimer
     */
    public void deleteEmploye(String matricule) {
        if (!employeRepository.existsById(matricule)) {
            throw new RuntimeException("Employé introuvable avec le matricule: " + matricule);
        }
        employeRepository.deleteById(matricule);
    }

    /**
     * Recherche des employés par nom
     * @param nom Nom ou partie du nom
     * @return Liste des employés correspondants
     */
    public List<Employe> searchEmployesByNom(String nom) {
        return employeRepository.findByNomContainingIgnoreCase(nom);
    }

    /**
     * Recherche des employés par prénom
     * @param prenom Prénom ou partie du prénom
     * @return Liste des employés correspondants
     */
    public List<Employe> searchEmployesByPrenom(String prenom) {
        return employeRepository.findByPrenomContainingIgnoreCase(prenom);
    }

    /**
     * Recherche des employés par nom ou prénom
     * @param terme Terme de recherche
     * @return Liste des employés correspondants
     */
    public List<Employe> searchEmployes(String terme) {
        return employeRepository.findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(terme, terme);
    }

    /**
     * Récupère les employés assignés à une tâche
     * @param idTache ID de la tâche
     * @return Liste des employés
     */
    public List<Employe> getEmployesByTache(Integer idTache) {
        return employeRepository.findEmployesByTacheId(idTache);
    }

    /**
     * Compte le nombre de tâches d'un employé
     * @param matricule Matricule de l'employé
     * @return Nombre de tâches
     */
    public Long countTachesByEmploye(String matricule) {
        return employeRepository.countTachesByEmployeMatricule(matricule);
    }
}