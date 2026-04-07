# Java Web Service — Yakamon

Projet EPITA JWS : API REST d'un jeu de type Pokémon appelé **Yakamon**, développé avec Quarkus et Java 21.

## Structure

```
.
├── database/    # Modèles JPA partagés (Student, Course)
├── endpoints/   # Endpoints REST basiques (HelloWorld, Reverse)
└── yakamon/     # Application principale du jeu
```

## Prérequis

- Java 21
- Maven
- PostgreSQL (base de données `yakamon` sur le port `5432`)

## Lancer l'application

```bash
cd yakamon
mvn quarkus:dev
```

L'API est disponible sur `http://localhost:8081`.

## API

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/start` | Démarrer une partie (reset DB + chargement de la map) |
| `GET` | `/player` | Informations du joueur |
| `POST` | `/move` | Déplacer le joueur (`UP`, `DOWN`, `LEFT`, `RIGHT`) |
| `POST` | `/collect` | Collecter la ressource sur la tuile courante |
| `POST` | `/catch` | Attraper un Yakamon sur la tuile courante |
| `GET` | `/inventory` | Inventaire du joueur |
| `GET` | `/team` | Équipe de Yakamons |
| `POST` | `/team/{uuid}/feed` | Nourrir un Yakamon |
| `POST` | `/team/{uuid}/evolve` | Faire évoluer un Yakamon |
| `PATCH` | `/team/{uuid}/rename` | Renommer un Yakamon |
| `DELETE` | `/team/{uuid}/release` | Relâcher un Yakamon |
| `GET` | `/yakadex` | Consulter le Yakadex |
| `GET` | `/yakadex/{id}` | Détails d'un Yakamon |

La spécification OpenAPI complète est disponible dans `yakamon/src/main/resources/openapi.yaml`.

## Configuration

La datasource est configurée dans `yakamon/src/main/resources/application.properties` :

```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/yakamon
quarkus.datasource.username=postgres
```
