package com.votrenom.recharge.controller;

import com.votrenom.recharge.model.Equipement;
import com.votrenom.recharge.model.Station;
import com.votrenom.recharge.service.EquipementService;
import com.votrenom.recharge.service.StationService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class StationController {
    private final StationService service;
    private final EquipementService equipementService;

    public StationController(StationService service, EquipementService equipementService) {
        this.service = service;
        this.equipementService = equipementService;
    }

    @GetMapping("/stations")
    public List<Station> all(@RequestParam(required = false) String ville) {
        return ville == null ? service.findAll() : service.findByVille(ville);
    }

    @GetMapping("/stations/{id}")
    public Station one(@PathVariable Long id) { return service.findById(id); }

    @PostMapping("/stations")
    public ResponseEntity<Station> create(@Valid @RequestBody Station s) {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(s));
    }

    @PutMapping("/stations/{id}")
    public Station update(@PathVariable Long id, @Valid @RequestBody Station s) {
        return service.update(id, s);
    }

    @DeleteMapping("/stations/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/stations/{id}/equipements/{equipementId}")
    public Station addEquipement(@PathVariable Long id, @PathVariable Long equipementId) {
        return service.ajouterEquipement(id, equipementId);
    }

    // Requête personnalisée 4
    @GetMapping("/stations/{id}/chiffre-affaires")
    public Map<String, Object> ca(@PathVariable Long id) {
        return Map.of("stationId", id, "chiffreAffaires", service.chiffreAffaires(id));
    }

    // --- CRUD Equipements ---
    @GetMapping("/equipements")
    public List<Equipement> equipements() { return equipementService.findAll(); }

    @GetMapping("/equipements/{id}")
    public Equipement equipement(@PathVariable Long id) { return equipementService.findById(id); }

    @PostMapping("/equipements")
    public ResponseEntity<Equipement> createEquipement(@Valid @RequestBody Equipement e) {
        return ResponseEntity.status(HttpStatus.CREATED).body(equipementService.create(e));
    }

    @PutMapping("/equipements/{id}")
    public Equipement updateEquipement(@PathVariable Long id, @Valid @RequestBody Equipement e) {
        return equipementService.update(id, e);
    }

    @DeleteMapping("/equipements/{id}")
    public ResponseEntity<Void> deleteEquipement(@PathVariable Long id) {
        equipementService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
