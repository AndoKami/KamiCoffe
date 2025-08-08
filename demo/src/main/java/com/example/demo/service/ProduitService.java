package com.example.demo.service;

import com.example.demo.entity.Produit;
import com.example.demo.repository.ProduitRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProduitService {

    private final ProduitRepository produitRepository;

    public ProduitService(ProduitRepository produitRepository) {
        this.produitRepository = produitRepository;
    }

    public Produit ajouterProduit(Produit produit) {
        return produitRepository.save(produit);
    }

    public List<Produit> listerProduits() {
        return produitRepository.findAll();
    }

    public Optional<Produit> trouverProduitParId(Long id) {
        return produitRepository.findById(id);
    }

    public Produit modifierProduit(Long id, Produit produit) {
        return produitRepository.findById(id)
                .map(p -> {
                    p.setNom(produit.getNom());
                    p.setPrix(produit.getPrix());
                    p.setDescription(produit.getDescription());
                    return produitRepository.save(p);
                })
                .orElseThrow(() -> new RuntimeException("Produit non trouvé"));
    }

    public void supprimerProduit(Long id) {
        produitRepository.deleteById(id);
    }
}
