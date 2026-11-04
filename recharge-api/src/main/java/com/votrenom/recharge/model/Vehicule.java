package com.votrenom.recharge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name = "vehicules")
public class Vehicule {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "L'immatriculation est obligatoire")
    @Column(unique = true)
    private String immatriculation;

    @NotBlank(message = "La marque est obligatoire")
    private String marque;

    @NotBlank(message = "Le modèle est obligatoire")
    private String modele;

    @NotNull(message = "Le type de connecteur est obligatoire")
    @Enumerated(EnumType.STRING)
    private TypeConnecteur typeConnecteur;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utilisateur_id", nullable = false)
    @JsonIgnore
    private Utilisateur utilisateur;

    public Vehicule() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public TypeConnecteur getTypeConnecteur() { return typeConnecteur; }
    public void setTypeConnecteur(TypeConnecteur typeConnecteur) { this.typeConnecteur = typeConnecteur; }

    public Utilisateur getUtilisateur() { return utilisateur; }
    public void setUtilisateur(Utilisateur utilisateur) { this.utilisateur = utilisateur; }
}
