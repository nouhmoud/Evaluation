package ma.projet.classes;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LigneCommandeProduit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int quantite;

    @ManyToOne
    @JoinColumn(name = "produit_id")
    private Produit produit;

    @ManyToOne
    @JoinColumn(name = "commande_id")
    private Commande commande;
}