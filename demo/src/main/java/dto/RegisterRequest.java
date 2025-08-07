package dto;

public class RegisterRequest {
    private String nom;
    private String email;
    private String motDePasse;

    public String getNom() {
        return nom;
    }
    public String getMotDePasse(){
        return motDePasse;
    }
     public String getEmail() {
        return email;
    }

}
