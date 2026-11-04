package com.votrenom.recharge.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

public record ReservationRequest(
        @NotNull(message = "utilisateurId obligatoire") Long utilisateurId,
        @NotNull(message = "vehiculeId obligatoire") Long vehiculeId,
        @NotNull(message = "borneId obligatoire") Long borneId,
        @NotNull(message = "dateDebut obligatoire")
        @Future(message = "La date de début doit être dans le futur") LocalDateTime dateDebut,
        @NotNull(message = "dateFin obligatoire") LocalDateTime dateFin
) {}
