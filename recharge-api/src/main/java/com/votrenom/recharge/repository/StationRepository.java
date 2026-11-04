package com.votrenom.recharge.repository;

import com.votrenom.recharge.model.Station;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StationRepository extends JpaRepository<Station, Long> {
    List<Station> findByVilleIgnoreCase(String ville);
}
