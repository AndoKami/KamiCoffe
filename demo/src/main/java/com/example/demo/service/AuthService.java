package com.example.demo.service;

import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    public User register(String nom, String email, String motDePasse) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email déjà utilisé");
        }

        String hash = BCrypt.hashpw(motDePasse, BCrypt.gensalt());
        User user = new User();
        user.setNom(nom);
        user.setEmail(email);
        user.setMotDePasse(BCrypt.hashpw(motDePasse, BCrypt.gensalt()));
        return userRepository.save(user);
    }

    public User login(String email, String motDePasse) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Email invalide"));

        if (!BCrypt.checkpw(motDePasse, user.getMotDePasse())) {
            throw new RuntimeException("Mot de passe incorrect");
        }

        return user;
    }
}
