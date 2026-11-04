package com.votrenom.recharge.controller;

import com.votrenom.recharge.model.Borne;
import com.votrenom.recharge.model.StatutBorne;
import com.votrenom.recharge.model.TypeConnecteur;
import com.votrenom.recharge.service.BorneService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api")
public class BorneController {
    private final BorneService service;

    public BorneController(BorneService service) {
        this.service = service;
    }

    @GetMapping("/bornes")
    public List<Borne> all() { return service.findAll(); }

    @GetMapping("/bornes/{id}")
    public Borne one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping("/stations/{stationId}/bornes")
    public ResponseEntity<Borne> create(@PathVariable Long stationId, @Valid @RequestBody Borne b) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(stationId, b));
    }

    @PutMapping("/bornes/{id}")
    public Borne update(@PathVariable Long id, @Valid @RequestBody Borne b) {
        return service.update(id, b);
    }

    @PatchMapping("/bornes/{id}/statut")
    public Borne statut(@PathVariable Long id, @RequestParam StatutBorne statut) {
        return service.changerStatut(id, statut);
    }

    @DeleteMapping("/bornes/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    // Requête personnalisée 1
    @GetMapping("/bornes/disponibles")
    public List<Borne> disponibles(@RequestParam String ville,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime debut,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fin) {
        return service.disponibles(ville, debut, fin);
    }

    // Requête personnalisée 2
    @GetMapping("/bornes/recherche")
    public List<Borne> recherche(@RequestParam TypeConnecteur type,
                                 @RequestParam(defaultValue = "0") double puissanceMin) {
        return service.recherche(type, puissanceMin);
    }
}
