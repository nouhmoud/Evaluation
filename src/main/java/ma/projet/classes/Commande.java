package ma.projet.classes;

import jakarta.persistence.*;
import lombok.*;
import java.util.Date;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Commande {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Temporal(TemporalType.DATE)
    private Date date;

    @OneToMany(mappedBy = "commande", cascade = CascadeType.ALL)
    private List<LigneCommandeProduit> lignesCommande;
}