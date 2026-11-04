package com.votrenom.recharge.service;

import com.votrenom.recharge.dto.ReservationRequest;
import com.votrenom.recharge.dto.StatStation;
import com.votrenom.recharge.exception.BusinessException;
import com.votrenom.recharge.exception.ResourceNotFoundException;
import com.votrenom.recharge.model.*;
import com.votrenom.recharge.repository.ReservationRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository repo;
    private final UtilisateurService utilisateurService;
    private final VehiculeService vehiculeService;
    private final BorneService borneService;

    public ReservationService(ReservationRepository repo, UtilisateurService utilisateurService, VehiculeService vehiculeService, BorneService borneService) {
        this.repo = repo;
        this.utilisateurService = utilisateurService;
        this.vehiculeService = vehiculeService;
        this.borneService = borneService;
    }

    public List<Reservation> findAll() { return repo.findAll(); }

    public Reservation findById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Réservation introuvable : " + id));
    }

    public List<Reservation> findByUtilisateur(Long userId) {
        utilisateurService.findById(userId);
        return repo.findByUtilisateurIdOrderByDateDebutDesc(userId);
    }

    @Transactional
    public Reservation creer(ReservationRequest req) {
        Utilisateur u = utilisateurService.findById(req.utilisateurId());
        Vehicule v = vehiculeService.findById(req.vehiculeId());
        Borne b = borneService.findById(req.borneId());

        if (!v.getUtilisateur().getId().equals(u.getId()))
            throw new BusinessException("Ce véhicule n'appartient pas à cet utilisateur");
        if (v.getTypeConnecteur() != b.getTypeConnecteur())
            throw new BusinessException("Connecteur incompatible entre le véhicule et la borne");
        if (b.getStatut() != StatutBorne.DISPONIBLE)
            throw new BusinessException("La borne n'est pas disponible (" + b.getStatut() + ")");

        verifierCreneau(b.getId(), req.dateDebut(), req.dateFin(), 0L);

        Reservation r = new Reservation();
        r.setUtilisateur(u);
        r.setVehicule(v);
        r.setBorne(b);
        r.setDateDebut(req.dateDebut());
        r.setDateFin(req.dateFin());
        r.setStatut(StatutReservation.CONFIRMEE);
        r.setMontant(calculerMontant(b, req.dateDebut(), req.dateFin()));
        return repo.save(r);
    }

    @Transactional
    public Reservation modifier(Long id, LocalDateTime debut, LocalDateTime fin) {
        Reservation r = findById(id);
        if (r.getStatut() != StatutReservation.CONFIRMEE)
            throw new BusinessException("Seule une réservation confirmée peut être modifiée");
        verifierCreneau(r.getBorne().getId(), debut, fin, r.getId());
        r.setDateDebut(debut);
        r.setDateFin(fin);
        r.setMontant(calculerMontant(r.getBorne(), debut, fin));
        return repo.save(r);
    }

    @Transactional
    public Reservation annuler(Long id) {
        Reservation r = findById(id);
        if (r.getStatut() == StatutReservation.ANNULEE)
            throw new BusinessException("Réservation déjà annulée");
        r.setStatut(StatutReservation.ANNULEE);
        return repo.save(r);
    }

    public void supprimer(Long id) { repo.delete(findById(id)); }

    public List<StatStation> statistiques() { return repo.statistiquesParStation(); }

    private void verifierCreneau(Long borneId, LocalDateTime debut, LocalDateTime fin, Long excludeId) {
        if (!fin.isAfter(debut))
            throw new BusinessException("La date de fin doit être après la date de début");
        if (Duration.between(debut, fin).toHours() > 8)
            throw new BusinessException("Durée maximale de réservation : 8 heures");
        if (repo.compterChevauchements(borneId, debut, fin, StatutReservation.CONFIRMEE, excludeId) > 0)
            throw new BusinessException("La borne est déjà réservée sur ce créneau");
    }

    BigDecimal calculerMontant(Borne b, LocalDateTime debut, LocalDateTime fin) {
        BigDecimal heures = BigDecimal.valueOf(Duration.between(debut, fin).toMinutes())
                .divide(BigDecimal.valueOf(60), 2, RoundingMode.HALF_UP);
        return b.getTarifHoraire().multiply(heures).setScale(2, RoundingMode.HALF_UP);
    }
}
