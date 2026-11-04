package com.votrenom.recharge.controller;

import com.votrenom.recharge.model.Reservation;
import com.votrenom.recharge.model.Utilisateur;
import com.votrenom.recharge.model.Vehicule;
import com.votrenom.recharge.service.ReservationService;
import com.votrenom.recharge.service.UtilisateurService;
import com.votrenom.recharge.service.VehiculeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class UtilisateurController {
    private final UtilisateurService service;
    private final VehiculeService vehiculeService;
    private final ReservationService reservationService;

    public UtilisateurController(UtilisateurService service, VehiculeService vehiculeService, ReservationService reservationService) {
        this.service = service;
        this.vehiculeService = vehiculeService;
        this.reservationService = reservationService;
    }

    @GetMapping("/utilisateurs")
    public List<Utilisateur> all() { return service.findAll(); }

    @GetMapping("/utilisateurs/{id}")
    public Utilisateur one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping("/utilisateurs")
    public ResponseEntity<Utilisateur> create(@Valid @RequestBody Utilisateur u) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(u));
    }

    @PutMapping("/utilisateurs/{id}")
    public Utilisateur update(@PathVariable Long id, @Valid @RequestBody Utilisateur u) {
        return service.update(id, u);
    }

    @DeleteMapping("/utilisateurs/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Véhicules ---
    @GetMapping("/utilisateurs/{id}/vehicules")
    public List<Vehicule> vehicules(@PathVariable Long id) { return vehiculeService.findByUtilisateur(id); }

    @PostMapping("/utilisateurs/{id}/vehicules")
    public ResponseEntity<Vehicule> addVehicule(@PathVariable Long id, @Valid @RequestBody Vehicule v) {
        return ResponseEntity.status(HttpStatus.CREATED).body(vehiculeService.create(id, v));
    }

    @GetMapping("/vehicules/{id}")
    public Vehicule vehicule(@PathVariable Long id) { return vehiculeService.findById(id); }

    @PutMapping("/vehicules/{id}")
    public Vehicule updateVehicule(@PathVariable Long id, @Valid @RequestBody Vehicule v) {
        return vehiculeService.update(id, v);
    }

    @DeleteMapping("/vehicules/{id}")
    public ResponseEntity<Void> deleteVehicule(@PathVariable Long id) {
        vehiculeService.delete(id);
        return ResponseEntity.noContent().build();
    }

    // --- Historique des réservations ---
    @GetMapping("/utilisateurs/{id}/reservations")
    public List<Reservation> reservations(@PathVariable Long id) {
        return reservationService.findByUtilisateur(id);
    }
}
