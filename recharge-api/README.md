# API de réservation de bornes de recharge électrique

## Contexte
API REST (Spring Boot) permettant de gérer des stations, bornes, utilisateurs, véhicules
et réservations de créneaux de recharge, avec contrôle des règles métier.

## Technologies
Java 17+, Spring Boot 3.3, Spring Data JPA, MySQL 8, Bean Validation, Postman.

## Lancer le projet
1. Installer JDK 17+, Maven et MySQL 8 (ou XAMPP/WAMP) puis démarrer MySQL.
2. Éditer `src/main/resources/application.properties` : `spring.datasource.username` / `password`.
   La base `recharge_db` est créée automatiquement (`createDatabaseIfNotExist=true`),
   sinon : `CREATE DATABASE recharge_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;`
3. Lancer : `mvn spring-boot:run` (ou exécuter `RechargeApplication` depuis l'IDE).
4. API disponible sur http://localhost:8081
5. Tests unitaires (sans base) : `mvn test`

### Avec Spring Tools
File → Import → Maven → Existing Maven Projects → choisir le dossier du projet → Finish.
Puis clic droit sur `RechargeApplication.java` → Run As → Spring Boot App.
(Le projet n'utilise pas Lombok : aucune installation supplémentaire n'est nécessaire.)

## Entités et relations
| Entité | Relations |
|---|---|
| Utilisateur | 1–N Vehicule, 1–N Reservation |
| Vehicule | N–1 Utilisateur |
| Station | 1–N Borne, N–N Equipement (table `station_equipement`) |
| Equipement | N–N Station |
| Borne | N–1 Station, 1–N Reservation |
| Reservation | N–1 Utilisateur, Borne, Vehicule |

## Règles métier
- Pas de chevauchement de créneaux sur une même borne (`debut1 < fin2 ET fin1 > debut2`)
- Date de fin postérieure à la date de début, durée max 8 h
- Borne `DISPONIBLE`, connecteur véhicule = connecteur borne, véhicule appartenant à l'utilisateur
- Montant = durée (h) × tarif horaire

## Endpoints (base `/api`)
| Méthode | URL | Description | Codes |
|---|---|---|---|
| GET/POST | /utilisateurs | Lister / créer | 200 / 201, 400, 409 |
| GET/PUT/DELETE | /utilisateurs/{id} | Lire / modifier / supprimer | 200, 204, 404 |
| GET/POST | /utilisateurs/{id}/vehicules | Véhicules d'un utilisateur | 200 / 201 |
| GET/PUT/DELETE | /vehicules/{id} | CRUD véhicule | 200, 204, 404 |
| GET | /utilisateurs/{id}/reservations | Historique | 200, 404 |
| GET/POST | /stations (`?ville=`) | Lister / créer | 200 / 201 |
| GET/PUT/DELETE | /stations/{id} | CRUD station | 200, 204, 404, 409 |
| POST | /stations/{id}/equipements/{eid} | Associer équipement (N–N) | 200 |
| GET | /stations/{id}/chiffre-affaires | **Requête 4** | 200 |
| GET/POST | /equipements | CRUD équipement | 200 / 201 |
| GET/PUT/DELETE | /equipements/{id} | CRUD équipement | 200, 204, 404 |
| GET | /bornes | Lister | 200 |
| POST | /stations/{id}/bornes | Créer une borne | 201, 400, 409 |
| GET/PUT/DELETE | /bornes/{id} | CRUD borne | 200, 204, 404 |
| PATCH | /bornes/{id}/statut?statut= | Changer le statut | 200 |
| GET | /bornes/disponibles?ville=&debut=&fin= | **Requête 1** | 200, 409 |
| GET | /bornes/recherche?type=&puissanceMin= | **Requête 2** | 200 |
| GET/POST | /reservations | Lister / réserver | 200 / 201, 400, 409 |
| GET/PUT/DELETE | /reservations/{id} | Lire / modifier créneau / supprimer | 200, 204, 404, 409 |
| PATCH | /reservations/{id}/annuler | Annuler | 200, 409 |
| GET | /reservations/statistiques | **Requête 3** | 200 |

Format des dates : `2026-12-01T10:00:00`.

## Gestion des erreurs
`400` validation / JSON invalide · `404` ressource introuvable · `409` règle métier ou contrainte violée · `500` erreur interne.
Réponse JSON : `{timestamp, status, erreur, message}`.

## Tests Postman
Importer `postman/recharge.postman_collection.json` et lancer la collection **dans l'ordre** (Runner).
Captures dans `postman/captures/`.

## Auteurs
Noms des membres du groupe
