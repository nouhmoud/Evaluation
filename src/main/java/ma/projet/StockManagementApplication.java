package ma.projet;

import ma.projet.classes.*;
import ma.projet.dao.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.text.SimpleDateFormat;
import java.util.Date;

@SpringBootApplication
public class StockManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(StockManagementApplication.class, args);
    }

    @Bean
    CommandLineRunner initDatabase(
            CategorieService categorieService,
            ProduitService produitService,
            CommandeService commandeService,
            LigneCommandeService ligneCommandeService) {

        return args -> {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");

            // Créer des catégories
            Categorie c1 = new Categorie();
            c1.setCode("CAT1");
            c1.setLibelle("Ordinateurs");
            categorieService.save(c1);

            Categorie c2 = new Categorie();
            c2.setCode("CAT2");
            c2.setLibelle("Imprimantes");
            categorieService.save(c2);

            // Créer des produits
            Produit p1 = new Produit();
            p1.setReference("S312");
            p1.setPrix(120.0);
            p1.setCategorie(c1);
            produitService.save(p1);

            Produit p2 = new Produit();
            p2.setReference("Z985");
            p2.setPrix(180.0);
            p2.setCategorie(c1);
            produitService.save(p2);

            Produit p3 = new Produit();
            p3.setReference("I185");
            p3.setPrix(280.0);
            p3.setCategorie(c2);
            produitService.save(p3);

            // Créer une commande
            Commande cmd = new Commande();
            cmd.setDate(sdf.parse("14/03/2013"));
            commandeService.save(cmd);

            // Créer des lignes de commande
            LigneCommandeProduit lc1 = new LigneCommandeProduit();
            lc1.setProduit(p1);
            lc1.setCommande(cmd);
            lc1.setQuantite(7);
            ligneCommandeService.save(lc1);

            LigneCommandeProduit lc2 = new LigneCommandeProduit();
            lc2.setProduit(p2);
            lc2.setCommande(cmd);
            lc2.setQuantite(14);
            ligneCommandeService.save(lc2);

            LigneCommandeProduit lc3 = new LigneCommandeProduit();
            lc3.setProduit(p3);
            lc3.setCommande(cmd);
            lc3.setQuantite(5);
            ligneCommandeService.save(lc3);

            System.out.println("=== Données initiales créées ===");

            // Tests des requêtes
            System.out.println("\n=== Produits par catégorie 1 ===");
            produitService.findProduitsByCategorie(1).forEach(p ->
                    System.out.println(p.getReference() + " - " + p.getPrix() + " DH")
            );

            System.out.println("\n=== Produits avec prix > 100 DH ===");
            produitService.findProduitsAvecPrixSuperieurA100().forEach(p ->
                    System.out.println(p.getReference() + " - " + p.getPrix() + " DH")
            );
        };
    }
}