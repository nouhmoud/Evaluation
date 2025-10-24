package ma.projet.classes;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Categorie {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String code;
    private String libelle;

    @OneToMany(mappedBy = "categorie", cascade = CascadeType.ALL)
    private List<Produit> produits;
}