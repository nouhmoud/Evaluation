package ma.projet.classes;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Produit {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String reference;
    private double prix;

    @ManyToOne
    @JoinColumn(name = "categorie_id")
    private Categorie categorie;

    @OneToMany(mappedBy = "produit", cascade = CascadeType.ALL)
    private List<LigneCommandeProduit> lignesCommande;
}