package com.votrenom.recharge.repository;

import com.votrenom.recharge.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    boolean existsByEmail(String email);
}
