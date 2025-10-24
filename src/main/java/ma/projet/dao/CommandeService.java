package ma.projet.dao;

import ma.projet.classes.Commande;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommandeService extends JpaRepository<Commande, Integer> {
}