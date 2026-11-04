package com.votrenom.recharge.service;

import com.votrenom.recharge.dto.ReservationRequest;
import com.votrenom.recharge.exception.BusinessException;
import com.votrenom.recharge.model.*;
import com.votrenom.recharge.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceTest {

    @Mock ReservationRepository repo;
    @Mock UtilisateurService utilisateurService;
    @Mock VehiculeService vehiculeService;
    @Mock BorneService borneService;
    @InjectMocks ReservationService service;

    Utilisateur user;
    Vehicule vehicule;
    Borne borne;
    LocalDateTime debut = LocalDateTime.now().plusDays(1).withHour(10).withMinute(0).withSecond(0).withNano(0);

    @BeforeEach
    void init() {
        user = new Utilisateur();
        user.setId(1L);
        vehicule = new Vehicule();
        vehicule.setId(1L);
        vehicule.setUtilisateur(user);
        vehicule.setTypeConnecteur(TypeConnecteur.TYPE2);
        borne = new Borne();
        borne.setId(1L);
        borne.setTypeConnecteur(TypeConnecteur.TYPE2);
        borne.setStatut(StatutBorne.DISPONIBLE);
        borne.setTarifHoraire(new BigDecimal("4.50"));

        lenient().when(utilisateurService.findById(1L)).thenReturn(user);
        lenient().when(vehiculeService.findById(1L)).thenReturn(vehicule);
        lenient().when(borneService.findById(1L)).thenReturn(borne);
    }

    @Test
    void creer_calculeLeMontant() {
        when(repo.compterChevauchements(anyLong(), any(), any(), any(), anyLong())).thenReturn(0L);
        when(repo.save(any(Reservation.class))).thenAnswer(i -> i.getArgument(0));

        Reservation r = service.creer(new ReservationRequest(1L, 1L, 1L, debut, debut.plusHours(2)));

        assertEquals(new BigDecimal("9.00"), r.getMontant());
        assertEquals(StatutReservation.CONFIRMEE, r.getStatut());
    }

    @Test
    void creer_refuseChevauchement() {
        when(repo.compterChevauchements(anyLong(), any(), any(), any(), anyLong())).thenReturn(1L);
        assertThrows(BusinessException.class,
                () -> service.creer(new ReservationRequest(1L, 1L, 1L, debut, debut.plusHours(2))));
    }

    @Test
    void creer_refuseConnecteurIncompatible() {
        borne.setTypeConnecteur(TypeConnecteur.CCS);
        assertThrows(BusinessException.class,
                () -> service.creer(new ReservationRequest(1L, 1L, 1L, debut, debut.plusHours(2))));
    }

    @Test
    void creer_refuseDateFinAvantDebut() {
        assertThrows(BusinessException.class,
                () -> service.creer(new ReservationRequest(1L, 1L, 1L, debut, debut.minusHours(1))));
    }
}
