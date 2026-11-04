package com.votrenom.recharge.repository;

import com.votrenom.recharge.dto.StatStation;
import com.votrenom.recharge.model.Reservation;
import com.votrenom.recharge.model.StatutReservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findByUtilisateurIdOrderByDateDebutDesc(Long utilisateurId);

    // Détection de chevauchement : debut1 < fin2 ET fin1 > debut2 (excludeId = 0 à la création)
    @Query("""
        SELECT COUNT(r) FROM Reservation r
        WHERE r.borne.id = :borneId AND r.statut = :statut AND r.id <> :excludeId
          AND r.dateDebut < :fin AND r.dateFin > :debut
        """)
    long compterChevauchements(@Param("borneId") Long borneId,
                               @Param("debut") LocalDateTime debut,
                               @Param("fin") LocalDateTime fin,
                               @Param("statut") StatutReservation statut,
                               @Param("excludeId") Long excludeId);

    // Requête personnalisée n°3 : statistiques par station
    @Query("""
        SELECT new com.votrenom.recharge.dto.StatStation(s.nom, COUNT(r))
        FROM Reservation r JOIN r.borne b JOIN b.station s
        GROUP BY s.nom ORDER BY COUNT(r) DESC
        """)
    List<StatStation> statistiquesParStation();

    // Requête personnalisée n°4 : chiffre d'affaires d'une station
    @Query("""
        SELECT COALESCE(SUM(r.montant), 0) FROM Reservation r
        WHERE r.borne.station.id = :stationId AND r.statut <> :annulee
        """)
    BigDecimal chiffreAffaires(@Param("stationId") Long stationId,
                               @Param("annulee") StatutReservation annulee);
}
