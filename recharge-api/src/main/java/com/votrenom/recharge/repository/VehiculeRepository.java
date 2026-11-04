package com.votrenom.recharge.repository;

import com.votrenom.recharge.model.Vehicule;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VehiculeRepository extends JpaRepository<Vehicule, Long> {
    List<Vehicule> findByUtilisateurId(Long utilisateurId);
    boolean existsByImmatriculation(String immatriculation);
}
