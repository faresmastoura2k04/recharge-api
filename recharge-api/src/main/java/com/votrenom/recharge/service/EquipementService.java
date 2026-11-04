package com.votrenom.recharge.service;

import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.Equipement;
import com.votrenom.recharge.repository.EquipementRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipementService {
    private final EquipementRepository repo;

    public EquipementService(EquipementRepository repo) {
        this.repo = repo;
    }

    public List<Equipement> findAll() { return repo.findAll(); }

    public Equipement findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Équipement introuvable : " + id));
    }

    public Equipement create(Equipement e) {
        e.setId(null);
        return repo.save(e);
    }

    public Equipement update(Long id, Equipement d) {
        Equipement e = findById(id);
        e.setLibelle(d.getLibelle());
        return repo.save(e);
    }

    public void delete(Long id) { repo.delete(findById(id)); }
}
