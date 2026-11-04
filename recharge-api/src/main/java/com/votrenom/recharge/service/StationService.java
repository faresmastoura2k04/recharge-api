package com.votrenom.recharge.service;

import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.Station;
import com.votrenom.recharge.model.StatutReservation;
import com.votrenom.recharge.repository.ReservationRepository;
import com.votrenom.recharge.repository.StationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
public class StationService {
    private final StationRepository repo;
    private final EquipementService equipementService;
    private final ReservationRepository reservationRepo;

    public StationService(StationRepository repo, EquipementService equipementService, ReservationRepository reservationRepo) {
        this.repo = repo;
        this.equipementService = equipementService;
        this.reservationRepo = reservationRepo;
    }

    public List<Station> findAll() { return repo.findAll(); }

    public List<Station> findByVille(String ville) { return repo.findByVilleIgnoreCase(ville); }

    public Station findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Station introuvable : " + id));
    }

    public Station create(Station s) {
        s.setId(null);
        return repo.save(s);
    }

    public Station update(Long id, Station d) {
        Station s = findById(id);
        s.setNom(d.getNom());
        s.setAdresse(d.getAdresse());
        s.setVille(d.getVille());
        return repo.save(s);
    }

    public void delete(Long id) { repo.delete(findById(id)); }

    @Transactional
    public Station ajouterEquipement(Long stationId, Long equipementId) {
        Station s = findById(stationId);
        s.getEquipements().add(equipementService.findById(equipementId));
        return repo.save(s);
    }

    public BigDecimal chiffreAffaires(Long id) {
        findById(id);
        return reservationRepo.chiffreAffaires(id, StatutReservation.ANNULEE);
    }
}
