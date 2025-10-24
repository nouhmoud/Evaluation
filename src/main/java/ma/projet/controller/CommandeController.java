package ma.projet.controller;

import ma.projet.classes.Commande;
import ma.projet.dao.CommandeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
@CrossOrigin(origins = "*")
public class CommandeController {

    @Autowired
    private CommandeService commandeService;

    // GET toutes les commandes
    @GetMapping
    public List<Commande> getAllCommandes() {
        return commandeService.findAll();
    }

    // GET une commande par ID
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getCommandeById(@PathVariable int id) {
        return commandeService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST créer une nouvelle commande
    @PostMapping
    public Commande createCommande(@RequestBody Commande commande) {
        return commandeService.save(commande);
    }

    // PUT mettre à jour une commande
    @PutMapping("/{id}")
    public ResponseEntity<Commande> updateCommande(@PathVariable int id, @RequestBody Commande commande) {
        return commandeService.findById(id)
                .map(existingCommande -> {
                    commande.setId(id);
                    return ResponseEntity.ok(commandeService.save(commande));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // DELETE supprimer une commande
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCommande(@PathVariable int id) {
        return commandeService.findById(id)
                .map(commande -> {
                    commandeService.deleteById(id);
                    return ResponseEntity.ok().<Void>build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}