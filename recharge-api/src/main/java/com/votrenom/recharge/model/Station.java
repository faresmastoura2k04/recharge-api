package com.votrenom.recharge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Table(name = "stations")
public class Station {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    private String nom;

    @NotBlank(message = "L'adresse est obligatoire")
    private String adresse;

    @NotBlank(message = "La ville est obligatoire")
    private String ville;

    @OneToMany(mappedBy = "station", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Borne> bornes = new ArrayList<>();

    @ManyToMany
    @JoinTable(name = "station_equipement",
            joinColumns = @JoinColumn(name = "station_id"),
            inverseJoinColumns = @JoinColumn(name = "equipement_id"))
    private Set<Equipement> equipements = new HashSet<>();

    public Station() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getAdresse() { return adresse; }
    public void setAdresse(String adresse) { this.adresse = adresse; }

    public String getVille() { return ville; }
    public void setVille(String ville) { this.ville = ville; }

    public List<Borne> getBornes() { return bornes; }
    public void setBornes(List<Borne> bornes) { this.bornes = bornes; }

    public Set<Equipement> getEquipements() { return equipements; }
    public void setEquipements(Set<Equipement> equipements) { this.equipements = equipements; }
}
