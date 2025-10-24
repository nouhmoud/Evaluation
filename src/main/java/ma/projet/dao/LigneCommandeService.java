package ma.projet.dao;

import ma.projet.classes.LigneCommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LigneCommandeService extends JpaRepository<LigneCommandeProduit, Integer> {
}