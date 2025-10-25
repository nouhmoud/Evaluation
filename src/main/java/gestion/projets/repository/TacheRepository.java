package gestion.projets.repository;

import gestion.projets.model.Tache;
import gestion.projets.model.Tache.StatutTache;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour l'entité Tache
 * Fournit les méthodes CRUD et des requêtes personnalisées
 */
@Repository
public interface TacheRepository extends JpaRepository<Tache, Integer> {

    /**
     * Trouve toutes les tâches d'un projet spécifique
     * @param idProjet ID du projet
     * @return Liste des tâches du projet
     */
    List<Tache> findByProjetIdProjet(Integer idProjet);

    /**
     * Trouve les tâches par statut
     * @param statut Statut recherché
     * @return Liste des tâches
     */
    List<Tache> findByStatut(StatutTache statut);

    /**
     * Trouve les tâches d'un projet par statut
     * @param idProjet ID du projet
     * @param statut Statut recherché
     * @return Liste des tâches
     */
    List<Tache> findByProjetIdProjetAndStatut(Integer idProjet, StatutTache statut);

    /**
     * Recherche des tâches par titre (contient, insensible à la casse)
     * @param titre Titre ou partie du titre
     * @return Liste des tâches correspondantes
     */
    List<Tache> findByTitreContainingIgnoreCase(String titre);

    /**
     * Trouve les tâches dont la date de fin est dépassée et le statut n'est pas TERMINEE
     * @param date Date de référence (généralement aujourd'hui)
     * @return Liste des tâches en retard
     */
    @Query("SELECT t FROM Tache t WHERE t.dateFin < :date AND t.statut != 'TERMINEE'")
    List<Tache> findTachesEnRetard(@Param("date") LocalDate date);

    /**
     * Trouve les tâches assignées à un employé spécifique
     * @param matricule Matricule de l'employé
     * @return Liste des tâches
     */
    @Query("SELECT t FROM Tache t JOIN t.affectations a WHERE a.employe.matricule = :matricule")
    List<Tache> findTachesByEmployeMatricule(@Param("matricule") String matricule);

    /**
     * Trouve les tâches entre deux dates
     * @param dateDebut Date de début
     * @param dateFin Date de fin
     * @return Liste des tâches
     */
    List<Tache> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Compte les tâches par statut pour un projet
     * @param idProjet ID du projet
     * @param statut Statut
     * @return Nombre de tâches
     */
    Long countByProjetIdProjetAndStatut(Integer idProjet, StatutTache statut);
}