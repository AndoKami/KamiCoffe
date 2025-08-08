package dto;

import java.util.List;

public class CreateCommandeRequest {
    private Long clientId;
    private List<CommandeProduitRequest> produits;

    public Long getClientId() { return clientId; }
    public void setClientId(Long clientId) { this.clientId = clientId; }

    public List<CommandeProduitRequest> getProduits() { return produits; }
    public void setProduits(List<CommandeProduitRequest> produits) { this.produits = produits; }
}
