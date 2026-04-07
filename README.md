# Java Web Service — Yakamon

Projet EPITA JWS : API REST d'un jeu de type Pokémon appelé **Yakamon**, développé avec Quarkus et Java 21.

## Structure

```
.
└── yakamon/    # Application principale du jeu
    ├── src/main/java/      # Code source (domain, data, presentation)
    ├── src/main/resources/ # Configuration, maps, OpenAPI spec
    └── src/test/           # Tests
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

## Frontend

Un frontend graphique existe pour jouer au jeu, cependant il est la propriété d'EPITA et ne peut pas être publié ici. Tous les endpoints de l'API sont néanmoins fonctionnels et peuvent être utilisés avec n'importe quel client HTTP.

## API

### Game

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `POST` | `/start` | Démarrer une partie — reset la DB et charge la map. Body : `{ "mapPath": "...", "playerName": "..." }`. Retourne la grille de tuiles. |

### Player

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/player` | Informations du joueur (position, timestamps des dernières actions). |
| `POST` | `/move` | Déplacer le joueur. Body : `{ "direction": "UP\|RIGHT\|DOWN\|LEFT" }`. Retourne la nouvelle position. `429` si trop récent. |
| `POST` | `/collect` | Collecter la ressource sur la tuile courante. Retourne la tuile mise à jour. `429` si trop récent. |
| `POST` | `/catch` | Attraper le Yakamon sur la tuile courante (nécessite des Yakaballs, équipe non pleine). `429` si trop récent. |

### Inventory

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/inventory` | Récupère l'inventaire du joueur (liste d'items avec quantités). |

### Team

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/team` | Récupère la liste des Yakamons dans l'équipe du joueur. |
| `POST` | `/team/{uuid}/feed` | Nourrir un Yakamon avec des Scrooge. Body : `{ "quantity": N }`. `429` si trop récent. |
| `POST` | `/team/{uuid}/evolve` | Faire évoluer un Yakamon (nécessite assez de points d'énergie). |
| `PATCH` | `/team/{uuid}/rename` | Renommer un Yakamon. Body : `{ "newNickname": "..." }` (max 20 caractères, non vide). |
| `DELETE` | `/team/{uuid}/release` | Relâcher un Yakamon dans la nature. `403` si c'est le dernier pouvant marcher sur la tuile courante. |

### Yakadex

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| `GET` | `/yakadex` | Récupère toutes les entrées du Yakadex. Query param optionnel : `only_missing=true`. |
| `GET` | `/yakadex/{id}` | Récupère les détails d'un Yakamon par son ID (types, seuil d'évolution, description). |

## Configuration

La datasource est configurée dans `yakamon/src/main/resources/application.properties` :

```properties
quarkus.datasource.jdbc.url=jdbc:postgresql://localhost:5432/yakamon
quarkus.datasource.username=postgres
```

La spécification OpenAPI complète est disponible dans `yakamon/src/main/resources/openapi.yaml`.
