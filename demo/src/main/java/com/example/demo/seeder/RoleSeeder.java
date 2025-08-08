package com.example.demo.seeder;

import com.example.demo.entity.Role;
import com.example.demo.repository.RoleRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RoleSeeder {

    private final RoleRepository roleRepository;

    public RoleSeeder(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @PostConstruct
    public void seedRoles() {
        List<String> roles = List.of("ADMIN", "CLIENT", "BARISTA");

        for (String nom : roles) {
            if (roleRepository.findByNom(nom).isEmpty()) {
                Role role = new Role();
                role.setNom(nom);
                roleRepository.save(role);
            }
        }
    }
}