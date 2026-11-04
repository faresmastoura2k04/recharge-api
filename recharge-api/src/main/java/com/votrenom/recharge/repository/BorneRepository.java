package com.votrenom.recharge.repository;

import com.votrenom.recharge.model.Borne;
import com.votrenom.recharge.model.StatutBorne;
import com.votrenom.recharge.model.StatutReservation;
import com.votrenom.recharge.model.TypeConnecteur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface BorneRepository extends JpaRepository<Borne, Long> {

    boolean existsByCode(String code);

    // Requête dérivée
    List<Borne> findByStationId(Long stationId);

    // Requête personnalisée n°2 (dérivée) : connecteur + puissance minimale
    List<Borne> findByTypeConnecteurAndPuissanceKwGreaterThanEqual(TypeConnecteur type, double puissance);

    // Requête personnalisée n°1 (JPQL) : bornes libres dans une ville sur un créneau
    @Query("""
        SELECT b FROM Borne b
        WHERE LOWER(b.station.ville) = LOWER(:ville)
          AND b.statut = :statut
          AND NOT EXISTS (
              SELECT r FROM Reservation r
              WHERE r.borne = b AND r.statut = :resa
                AND r.dateDebut < :fin AND r.dateFin > :debut)
        """)
    List<Borne> findDisponibles(@Param("ville") String ville,
                                @Param("debut") LocalDateTime debut,
                                @Param("fin") LocalDateTime fin,
                                @Param("statut") StatutBorne statut,
                                @Param("resa") StatutReservation resa);
}
