package gestion.projets.repository;

import gestion.projets.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repository pour l'entité Employe
 * Fournit les méthodes CRUD et des requêtes personnalisées
 */
@Repository
public interface EmployeRepository extends JpaRepository<Employe, String> {

    /**
     * Recherche un employé par email
     * @param email Email de l'employé
     * @return Optional contenant l'employé si trouvé
     */
    Optional<Employe> findByEmail(String email);

    /**
     * Recherche des employés par nom (contient, insensible à la casse)
     * @param nom Nom ou partie du nom
     * @return Liste des employés correspondants
     */
    List<Employe> findByNomContainingIgnoreCase(String nom);

    /**
     * Recherche des employés par prénom (contient, insensible à la casse)
     * @param prenom Prénom ou partie du prénom
     * @return Liste des employés correspondants
     */
    List<Employe> findByPrenomContainingIgnoreCase(String prenom);

    /**
     * Recherche des employés par nom OU prénom
     * @param nom Nom
     * @param prenom Prénom
     * @return Liste des employés correspondants
     */
    List<Employe> findByNomContainingIgnoreCaseOrPrenomContainingIgnoreCase(String nom, String prenom);

    /**
     * Vérifie si un email existe déjà
     * @param email Email à vérifier
     * @return true si l'email existe
     */
    boolean existsByEmail(String email);

    /**
     * Trouve les employés assignés à une tâche spécifique
     * @param idTache ID de la tâche
     * @return Liste des employés
     */
    @Query("SELECT DISTINCT e FROM Employe e JOIN e.affectations a WHERE a.tache.idTache = :idTache")
    List<Employe> findEmployesByTacheId(@Param("idTache") Integer idTache);

    /**
     * Compte le nombre de tâches d'un employé
     * @param matricule Matricule de l'employé
     * @return Nombre de tâches
     */
    @Query("SELECT COUNT(a) FROM EmployeeTache a WHERE a.employe.matricule = :matricule")
    Long countTachesByEmployeMatricule(@Param("matricule") String matricule);
}