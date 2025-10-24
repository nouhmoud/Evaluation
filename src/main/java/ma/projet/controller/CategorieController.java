package ma.projet.controller;

import ma.projet.classes.Categorie;
import ma.projet.dao.CategorieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/categories")
@CrossOrigin(origins = "*")
public class CategorieController {

    @Autowired
    private CategorieService categorieService;

    // GET toutes les catégories
    @GetMapping
    public List<Categorie> getAllCategories() {
        return categorieService.findAll();
    }

    // GET une catégorie par ID
    @GetMapping("/{id}")
    public ResponseEntity<Categorie> getCategorieById(@PathVariable int id) {
        return categorieService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer une nouvelle catégorie
    @PostMapping
    public Categorie createCategorie(@RequestBody Categorie categorie) {
        return categorieService.save(categorie);
    }

    // PUT mettre à jour une catégorie
    @PutMapping("/{id}")
    public ResponseEntity<Categorie> updateCategorie(@PathVariable int id, @RequestBody Categorie categorie) {
        return categorieService.findById(id)
                .map(existingCategorie -> {
                    categorie.setId(id);
                    return ResponseEntity.ok(categorieService.save(categorie));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer une catégorie
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategorie(@PathVariable int id) {
        return categorieService.findById(id)
                .map(categorie -> {
                    categorieService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}