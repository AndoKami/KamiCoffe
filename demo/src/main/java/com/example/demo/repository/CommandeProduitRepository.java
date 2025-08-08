package com.example.demo.repository;

import com.example.demo.entity.CommandeProduit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommandeProduitRepository extends JpaRepository<CommandeProduit, Long> {
}
