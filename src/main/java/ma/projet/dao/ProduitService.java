package ma.projet.dao;

import ma.projet.classes.Produit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.Date;
import java.util.List;

@Repository
public interface ProduitService extends JpaRepository<Produit, Integer> {

    // Trouver les produits commandés entre deux dates
    @Query("SELECT DISTINCT p FROM Produit p " +
            "JOIN p.lignesCommande lc " +
            "JOIN lc.commande c " +
            "WHERE c.date BETWEEN :startDate AND :endDate")
    List<Produit> findProduitsCommandesEntreDates(
            @Param("startDate") Date startDate,
            @Param("endDate") Date endDate
    );

    // Trouver les produits par catégorie
    @Query("SELECT p FROM Produit p WHERE p.categorie.id = :categorieId")
    List<Produit> findProduitsByCategorie(@Param("categorieId") int categorieId);

    // Trouver les produits avec prix > 100 DH
    @Query("SELECT p FROM Produit p WHERE p.prix > 100")
    List<Produit> findProduitsAvecPrixSuperieurA100();

    // Trouver les produits commandés dans une commande donnée
    @Query("SELECT p FROM Produit p " +
            "JOIN p.lignesCommande lc " +
            "WHERE lc.commande.id = :commandeId")
    List<Produit> findProduitsByCommande(@Param("commandeId") int commandeId);
}