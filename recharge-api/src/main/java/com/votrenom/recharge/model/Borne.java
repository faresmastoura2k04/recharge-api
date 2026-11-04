package com.votrenom.recharge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "bornes")
public class Borne {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le code est obligatoire")
    @Column(unique = true)
    private String code;

    @Positive(message = "La puissance doit être positive")
    private double puissanceKw;

    @NotNull(message = "Le type de connecteur est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeConnecteur typeConnecteur;

    @Enumerated(EnumType.STRING)
    private StatutBorne statut = StatutBorne.DISPONIBLE;

    @NotNull(message = "Le tarif horaire est obligatoire")
    @DecimalMin(value = "0.0", inclusive = false, message = "Le tarif doit être > 0")
    private BigDecimal tarifHoraire;

    @ManyToOne
    @JoinColumn(name = "station_id", nullable = false)
    @JsonIgnoreProperties({"equipements"})
    private Station station;

    @OneToMany(mappedBy = "borne")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public Borne() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public double getPuissanceKw() { return puissanceKw; }
    public void setPuissanceKw(double puissanceKw) { this.puissanceKw = puissanceKw; }

    public TypeConnecteur getTypeConnecteur() { return typeConnecteur; }
    public void setTypeConnecteur(TypeConnecteur typeConnecteur) { this.typeConnecteur = typeConnecteur; }

    public StatutBorne getStatut() { return statut; }
    public void setStatut(StatutBorne statut) { this.statut = statut; }

    public BigDecimal getTarifHoraire() { return tarifHoraire; }
    public void setTarifHoraire(BigDecimal tarifHoraire) { this.tarifHoraire = tarifHoraire; }

    public Station getStation() { return station; }
    public void setStation(Station station) { this.station = station; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
