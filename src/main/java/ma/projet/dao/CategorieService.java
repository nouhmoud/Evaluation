package ma.projet.dao;

import ma.projet.classes.Categorie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategorieService extends JpaRepository<Categorie, Integer> {
}