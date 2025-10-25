package gestion.projets.repository;

import gestion.projets.model.Projet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * Repository pour l'entité Projet
 * Fournit les méthodes CRUD et des requêtes personnalisées
 */
@Repository
public interface ProjetRepository extends JpaRepository<Projet, Integer> {

    /**
     * Recherche des projets par nom (contient, insensible à la casse)
     * @param nom Nom ou partie du nom à rechercher
     * @return Liste des projets correspondants
     */
    List<Projet> findByNomContainingIgnoreCase(String nom);

    /**
     * Trouve les projets dont la date de début est dans une plage donnée
     * @param dateDebut Date de début de la plage
     * @param dateFin Date de fin de la plage
     * @return Liste des projets
     */
    List<Projet> findByDateDebutBetween(LocalDate dateDebut, LocalDate dateFin);

    /**
     * Trouve les projets actifs (dont la date de fin est après aujourd'hui ou null)
     * @param aujourdhui Date du jour
     * @return Liste des projets actifs
     */
    @Query("SELECT p FROM Projet p WHERE p.dateFin IS NULL OR p.dateFin >= :aujourdhui")
    List<Projet> findProjetsActifs(@Param("aujourdhui") LocalDate aujourdhui);

    /**
     * Compte le nombre de tâches d'un projet
     * @param idProjet ID du projet
     * @return Nombre de tâches
     */
    @Query("SELECT COUNT(t) FROM Tache t WHERE t.projet.idProjet = :idProjet")
    Long countTachesByProjetId(@Param("idProjet") Integer idProjet);

    /**
     * Trouve les projets sans date de fin définie
     * @return Liste des projets
     */
    List<Projet> findByDateFinIsNull();
}