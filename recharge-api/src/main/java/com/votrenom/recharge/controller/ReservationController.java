package com.votrenom.recharge.controller;

import com.votrenom.recharge.dto.ReservationRequest;
import com.votrenom.recharge.dto.StatStation;
import com.votrenom.recharge.model.Reservation;
import com.votrenom.recharge.service.ReservationService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService service;

    public ReservationController(ReservationService service) {
        this.service = service;
    }

    @GetMapping
    public List<Reservation> all() { return service.findAll(); }

    // Requête personnalisée 3 (déclarée avant /{id} pour plus de clarté)
    @GetMapping("/statistiques")
    public List<StatStation> stats() { return service.statistiques(); }

    @GetMapping("/{id}")
    public Reservation one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping
    public ResponseEntity<Reservation> create(@Valid @RequestBody ReservationRequest req) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.creer(req));
    }

    @PutMapping("/{id}")
    public Reservation modifier(@PathVariable Long id,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return service.modifier(id, debut, fin);
    }

    @PatchMapping("/{id}/annuler")
    public Reservation annuler(@PathVariable Long id) { return service.annuler(id); }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.supprimer(id);
        return ResponseEntity.noContent().build();
    }
}
