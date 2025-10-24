package ma.projet.controller;

import ma.projet.classes.Produit;
import ma.projet.dao.ProduitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/produits")
@CrossOrigin(origins = "*")
public class ProduitController {

    @Autowired
    private ProduitService produitService;

    // GET tous les produits
    @GetMapping
    public List<Produit> getAllProduits() {
        return produitService.findAll();
    }

    // GET un produit par ID
    @GetMapping("/{id}")
    public ResponseEntity<Produit> getProduitById(@PathVariable int id) {
        return produitService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer un nouveau produit
    @PostMapping
    public Produit createProduit(@RequestBody Produit produit) {
        return produitService.save(produit);
    }

    // PUT mettre à jour un produit
    @PutMapping("/{id}")
    public ResponseEntity<Produit> updateProduit(@PathVariable int id, @RequestBody Produit produit) {
        return produitService.findById(id)
                .map(existingProduit -> {
                    produit.setId(id);
                    return ResponseEntity.ok(produitService.save(produit));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer un produit
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduit(@PathVariable int id) {
        return produitService.findById(id)
                .map(produit -> {
                    produitService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // GET produits par catégorie
    @GetMapping("/categorie/{categorieId}")
    public List<Produit> getProduitsByCategorie(@PathVariable int categorieId) {
        return produitService.findProduitsByCategorie(categorieId);
    }

    // GET produits avec prix > 100 DH
    @GetMapping("/prix-superieur-100")
    public List<Produit> getProduitsAvecPrixSuperieurA100() {
        return produitService.findProduitsAvecPrixSuperieurA100();
    }

    // GET produits commandés entre deux dates
    @GetMapping("/commandes-entre-dates")
    public List<Produit> getProduitsCommandesEntreDates(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date endDate) {
        return produitService.findProduitsCommandesEntreDates(startDate, endDate);
    }

    // GET produits d'une commande spécifique
    @GetMapping("/commande/{commandeId}")
    public List<Produit> getProduitsByCommande(@PathVariable int commandeId) {
        return produitService.findProduitsByCommande(commandeId);
    }
}