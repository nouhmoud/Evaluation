package ma.projet;

import ma.projet.classes.Categorie;
import ma.projet.classes.Produit;
import ma.projet.dao.CategorieService;
import ma.projet.dao.ProduitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class StockManagementApplicationTests {

    @Autowired
    private ProduitService produitService;

    @Autowired
    private CategorieService categorieService;

    @Test
    void contextLoads() {
        // Vérifie que le contexte Spring Boot se charge correctement
        assertNotNull(produitService);
        assertNotNull(categorieService);
    }

    @Test
    void testFindProduitsAvecPrixSuperieurA100() {
        // Test de la requête pour les produits avec prix > 100 DH
        List<Produit> produits = produitService.findProduitsAvecPrixSuperieurA100();

        assertNotNull(produits);
        assertFalse(produits.isEmpty());

        // Vérifie que tous les produits ont un prix > 100
        produits.forEach(p -> assertTrue(p.getPrix() > 100));
    }

    @Test
    void testFindProduitsByCategorie() {
        // Récupère toutes les catégories
        List<Categorie> categories = categorieService.findAll();

        assertNotNull(categories);
        assertFalse(categories.isEmpty());

        // Test pour la première catégorie
        Categorie firstCategorie = categories.get(0);
        List<Produit> produits = produitService.findProduitsByCategorie(firstCategorie.getId());

        assertNotNull(produits);
        // Vérifie que tous les produits appartiennent à cette catégorie
        produits.forEach(p ->
                assertEquals(firstCategorie.getId(), p.getCategorie().getId())
        );
    }

    @Test
    void testCreateAndRetrieveProduit() {
        // Crée une catégorie de test
        Categorie testCategorie = new Categorie();
        testCategorie.setCode("TEST");
        testCategorie.setLibelle("Test Catégorie");
        testCategorie = categorieService.save(testCategorie);

        // Crée un produit de test
        Produit testProduit = new Produit();
        testProduit.setReference("TEST123");
        testProduit.setPrix(150.0);
        testProduit.setCategorie(testCategorie);
        testProduit = produitService.save(testProduit);

        // Vérifie la récupération
        Produit retrieved = produitService.findById(testProduit.getId()).orElse(null);

        assertNotNull(retrieved);
        assertEquals("TEST123", retrieved.getReference());
        assertEquals(150.0, retrieved.getPrix());
        assertEquals(testCategorie.getId(), retrieved.getCategorie().getId());
    }

    @Test
    void testUpdateProduit() {
        // Récupère un produit existant
        List<Produit> produits = produitService.findAll();
        assertFalse(produits.isEmpty());

        Produit produit = produits.get(0);
        double nouveauPrix = 999.99;

        // Modifie le prix
        produit.setPrix(nouveauPrix);
        produitService.save(produit);

        // Vérifie la modification
        Produit updated = produitService.findById(produit.getId()).orElse(null);
        assertNotNull(updated);
        assertEquals(nouveauPrix, updated.getPrix());
    }

    @Test
    void testDeleteProduit() {
        // Crée un produit temporaire
        Categorie categorie = categorieService.findAll().get(0);

        Produit tempProduit = new Produit();
        tempProduit.setReference("TEMP999");
        tempProduit.setPrix(50.0);
        tempProduit.setCategorie(categorie);
        tempProduit = produitService.save(tempProduit);

        int tempId = tempProduit.getId();

        // Supprime le produit
        produitService.deleteById(tempId);

        // Vérifie la suppression
        assertFalse(produitService.findById(tempId).isPresent());
    }
}