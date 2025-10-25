package gestion.projets.repository;

import gestion.projets.model.EmployeeTache;
import gestion.projets.model.EmployeeTache.EmployeeTacheId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité EmployeeTache
 * Gère la relation Many-to-Many entre Employe et Tache
 */
@Repository
public interface EmployeeTacheRepository extends JpaRepository<EmployeeTache, EmployeeTacheId> {

    /**
     * Trouve toutes les affectations d'un employé
     * @param matricule Matricule de l'employé
     * @return Liste des affectations
     */
    List<EmployeeTache> findByEmployeMatricule(String matricule);

    /**
     * Trouve toutes les affectations d'une tâche
     * @param idTache ID de la tâche
     * @return Liste des affectations
     */
    List<EmployeeTache> findByTacheIdTache(Integer idTache);

    /**
     * Supprime toutes les affectations d'un employé
     * @param matricule Matricule de l'employé
     */
    void deleteByEmployeMatricule(String matricule);

    /**
     * Supprime toutes les affectations d'une tâche
     * @param idTache ID de la tâche
     */
    void deleteByTacheIdTache(Integer idTache);

    /**
     * Compte le nombre d'employés sur une tâche
     * @param idTache ID de la tâche
     * @return Nombre d'employés
     */
    Long countByTacheIdTache(Integer idTache);

    /**
     * Vérifie si un employé est déjà affecté à une tâche
     * @param matricule Matricule de l'employé
     * @param idTache ID de la tâche
     * @return true si l'affectation existe
     */
    boolean existsByEmployeMatriculeAndTacheIdTache(String matricule, Integer idTache);
}