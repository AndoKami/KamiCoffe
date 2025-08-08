package com.example.demo.controller;

import dto.CreateCommandeRequest;
import com.example.demo.entity.Commande;
import com.example.demo.entity.StatutCommande;
import com.example.demo.service.CommandeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/commandes")
public class CommandeController {

    private final CommandeService commandeService;

    public CommandeController(CommandeService commandeService) {
        this.commandeService = commandeService;
    }

    // Créer une commande (client)
    @PostMapping
    public ResponseEntity<Commande> createCommande(@RequestBody CreateCommandeRequest request) {
        Commande created = commandeService.createCommande(request);
        return ResponseEntity.status(201).body(created);
    }

    // Lister toutes les commandes (admin / barista usage)
    @GetMapping
    public ResponseEntity<List<Commande>> listAll() {
        return ResponseEntity.ok(commandeService.listAll());
    }

    // Récupérer une commande par id
    @GetMapping("/{id}")
    public ResponseEntity<Commande> getById(@PathVariable Long id) {
        return ResponseEntity.ok(commandeService.getById(id));
    }

    // Changer le statut (ex: EN_PREPARATION, TERMINEE) - accessible au barista/admin
    @PutMapping("/{id}/statut")
    public ResponseEntity<Commande> updateStatut(@PathVariable Long id, @RequestParam StatutCommande statut) {
        Commande updated = commandeService.updateStatut(id, statut);
        return ResponseEntity.ok(updated);
    }

    // Historique d'un client
    @GetMapping("/client/{clientId}")
    public ResponseEntity<List<Commande>> listByClient(@PathVariable Long clientId) {
        return ResponseEntity.ok(commandeService.listByClientId(clientId));
    }
}
