package com.votrenom.recharge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "utilisateurs")
public class Utilisateur {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Le nom est obligatoire")
    @Size(min = 2, max = 50, message = "Le nom doit contenir entre 2 et 50 caractères")
    private String nom;

    @NotBlank(message = "Le prénom est obligatoire")
    private String prenom;

    @NotBlank(message = "L'email est obligatoire")
    @Email(message = "Email invalide")
    @Column(unique = true, nullable = false)
    private String email;

    @Pattern(regexp = "^[0-9]{8}$", message = "Le téléphone doit contenir 8 chiffres")
    private String telephone;

    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnore
    private List<Vehicule> vehicules = new ArrayList<>();

    @OneToMany(mappedBy = "utilisateur")
    @JsonIgnore
    private List<Reservation> reservations = new ArrayList<>();

    public Utilisateur() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getTelephone() { return telephone; }
    public void setTelephone(String telephone) { this.telephone = telephone; }

    public List<Vehicule> getVehicules() { return vehicules; }
    public void setVehicules(List<Vehicule> vehicules) { this.vehicules = vehicules; }

    public List<Reservation> getReservations() { return reservations; }
    public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}
