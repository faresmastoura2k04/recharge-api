package com.votrenom.recharge.service;

import com.votrenom.recharge.exception.BusinessException;
import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.Vehicule;
import com.votrenom.recharge.repository.VehiculeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class VehiculeService {
    private final VehiculeRepository repo;
    private final UtilisateurService utilisateurService;

    public VehiculeService(VehiculeRepository repo, UtilisateurService utilisateurService) {
        this.repo = repo;
        this.utilisateurService = utilisateurService;
    }

    public List<Vehicule> findByUtilisateur(Long userId) {
        utilisateurService.findById(userId);
        return repo.findByUtilisateurId(userId);
    }

    public Vehicule findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Véhicule introuvable : " + id));
    }

    public Vehicule create(Long userId, Vehicule v) {
        if (repo.existsByImmatriculation(v.getImmatriculation()))
            throw new BusinessException("Immatriculation déjà enregistrée");
        v.setId(null);
        v.setUtilisateur(utilisateurService.findById(userId));
        return repo.save(v);
    }

    public Vehicule update(Long id, Vehicule d) {
        Vehicule v = findById(id);
        v.setImmatriculation(d.getImmatriculation());
        v.setMarque(d.getMarque());
        v.setModele(d.getModele());
        v.setTypeConnecteur(d.getTypeConnecteur());
        return repo.save(v);
    }

    public void delete(Long id) { repo.delete(findById(id)); }
}
