package com.votrenom.recharge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "equipements")
public class Equipement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le libellé est obligatoire")
    @Column(unique = true)
    private String libelle;

    @ManyToMany(mappedBy = "equipements")
    @JsonIgnore
    private Set<Station> stations = new HashSet<>();

    public Equipement() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getLibelle() { return libelle; }
    public void setLibelle(String libelle) { this.libelle = libelle; }

    public Set<Station> getStations() { return stations; }
    public void setStations(Set<Station> stations) { this.stations = stations; }
}
