package com.votrenom.recharge.service;

import com.votrenom.recharge.exception.BusinessException;
import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.Utilisateur;
import com.votrenom.recharge.repository.UtilisateurRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UtilisateurService {
    private final UtilisateurRepository repo;

    public UtilisateurService(UtilisateurRepository repo) {
        this.repo = repo;
    }

    public List<Utilisateur> findAll() { return repo.findAll(); }

    public Utilisateur findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Utilisateur introuvable : " + id));
    }

    public Utilisateur create(Utilisateur u) {
        if (repo.existsByEmail(u.getEmail()))
            throw new BusinessException("Email déjà utilisé : " + u.getEmail());
        u.setId(null);
        return repo.save(u);
    }

    public Utilisateur update(Long id, Utilisateur d) {
        Utilisateur u = findById(id);
        u.setNom(d.getNom());
        u.setPrenom(d.getPrenom());
        u.setEmail(d.getEmail());
        u.setTelephone(d.getTelephone());
        return repo.save(u);
    }

    public void delete(Long id) { repo.delete(findById(id)); }
}
