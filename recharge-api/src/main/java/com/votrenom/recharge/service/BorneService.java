package com.votrenom.recharge.service;

import com.votrenom.recharge.exception.BusinessException;
import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.Borne;
import com.votrenom.recharge.model.StatutBorne;
import com.votrenom.recharge.model.StatutReservation;
import com.votrenom.recharge.model.TypeConnecteur;
import com.votrenom.recharge.repository.BorneRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class BorneService {
    private final BorneRepository repo;
    private final StationService stationService;

    public BorneService(BorneRepository repo, StationService stationService) {
        this.repo = repo;
        this.stationService = stationService;
    }

    public List<Borne> findAll() { return repo.findAll(); }

    public Borne findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Borne introuvable : " + id));
    }

    public Borne create(Long stationId, Borne b) {
        if (repo.existsByCode(b.getCode()))
            throw new BusinessException("Code de borne déjà utilisé : " + b.getCode());
        b.setId(null);
        b.setStation(stationService.findById(stationId));
        return repo.save(b);
    }

    public Borne update(Long id, Borne d) {
        Borne b = findById(id);
        b.setCode(d.getCode());
        b.setPuissanceKw(d.getPuissanceKw());
        b.setTypeConnecteur(d.getTypeConnecteur());
        b.setTarifHoraire(d.getTarifHoraire());
        if (d.getStatut() != null) b.setStatut(d.getStatut());
        return repo.save(b);
    }

    public Borne changerStatut(Long id, StatutBorne statut) {
        Borne b = findById(id);
        b.setStatut(statut);
        return repo.save(b);
    }

    public void delete(Long id) { repo.delete(findById(id)); }

    public List<Borne> disponibles(String ville, LocalDateTime debut, LocalDateTime fin) {
        if (!fin.isAfter(debut))
            throw new BusinessException("La date de fin doit être après la date de début");
        return repo.findDisponibles(ville, debut, fin, StatutBorne.DISPONIBLE, StatutReservation.CONFIRMEE);
    }

    public List<Borne> recherche(TypeConnecteur type, double puissanceMin) {
        return repo.findByTypeConnecteurAndPuissanceKwGreaterThanEqual(type, puissanceMin);
    }
}
