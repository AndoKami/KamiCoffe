package com.example.demo.service;


import com.example.demo.repository.CommandeProduitRepository;
import com.example.demo.repository.CommandeRepository;
import dto.CreateCommandeRequest;
import dto.CommandeProduitRequest;

import com.example.demo.entity.*;
import com.example.demo.repository.ProduitRepository;
import com.example.demo.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandeService {

    private final CommandeRepository commandeRepository;
    private final ProduitRepository produitRepository;
    private final UserRepository userRepository;
    private final CommandeProduitRepository commandeProduitRepository;

    public CommandeService(CommandeRepository commandeRepository,
                           ProduitRepository produitRepository,
                           UserRepository userRepository,
                           CommandeProduitRepository commandeProduitRepository) {
        this.commandeRepository = commandeRepository;
        this.produitRepository = produitRepository;
        this.userRepository = userRepository;
        this.commandeProduitRepository = commandeProduitRepository;
    }

    @Transactional
    public Commande createCommande(CreateCommandeRequest request) {
        User client = userRepository.findById(request.getClientId())
                .orElseThrow(() -> new RuntimeException("Client introuvable"));

        if (request.getProduits() == null || request.getProduits().isEmpty()) {
            throw new RuntimeException("La commande doit contenir au moins un produit");
        }

        Commande commande = new Commande();
        commande.setClient(client);

        BigDecimal total = BigDecimal.ZERO;

        List<CommandeProduit> ligneProduits = request.getProduits().stream().map(item -> {
            Produit produit = produitRepository.findById(item.getProduitId())
                    .orElseThrow(() -> new RuntimeException("Produit introuvable id=" + item.getProduitId()));

            CommandeProduit cp = new CommandeProduit();
            cp.setProduit(produit);
            cp.setQuantite(item.getQuantite());
            cp.setPrixUnitaire(produit.getPrix());
            // back ref set later via addProduit
            // Optional: check stock and decrement here if you want

            BigDecimal ligne = produit.getPrix().multiply(BigDecimal.valueOf(item.getQuantite()));
            return new LigneWrapper(cp, ligne);
        }).map(LigneWrapper::getCp).collect(Collectors.toList());

        // Recompute total separately (to avoid double mapping complexity)
        total = request.getProduits().stream()
                .map(item -> {
                    Produit p = produitRepository.findById(item.getProduitId()).get(); // safe since fetched above
                    return p.getPrix().multiply(BigDecimal.valueOf(item.getQuantite()));
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // attach produits to commande
        ligneProduits.forEach(commande::addProduit);

        commande.setMontantTotal(total);

        Commande saved = commandeRepository.save(commande);

        // Ensure CommandeProduit persisted (cascade saves them), but we can save explicitly if needed
        // ligneProduits.forEach(cp -> commandeProduitRepository.save(cp));

        return saved;
    }

    public List<Commande> listAll() {
        return commandeRepository.findAll();
    }

    public Commande getById(Long id) {
        return commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
    }

    @Transactional
    public Commande updateStatut(Long id, StatutCommande statut) {
        Commande commande = commandeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Commande non trouvée"));
        commande.setStatut(statut);
        return commandeRepository.save(commande);
    }

    public List<Commande> listByClientId(Long clientId) {
        User client = userRepository.findById(clientId)
                .orElseThrow(() -> new RuntimeException("Client introuvable"));
        return commandeRepository.findByClient(client);
    }

    // helper wrapper to compute line (kept package-private)
    private static class LigneWrapper {
        private final CommandeProduit cp;
        private final BigDecimal montant;

        public LigneWrapper(CommandeProduit cp, BigDecimal montant) {
            this.cp = cp;
            this.montant = montant;
        }
        public CommandeProduit getCp() { return cp; }
        public BigDecimal getMontant() { return montant; }
    }
}
