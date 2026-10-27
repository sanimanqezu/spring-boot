# Users API

[![CI](https://github.com/sanimanqezu/spring-boot/actions/workflows/ci.yml/badge.svg)](https://github.com/sanimanqezu/spring-boot/actions/workflows/ci.yml)

A production-style **Spring Boot REST API** for managing users, backed by PostgreSQL with versioned schema migrations. Built to demonstrate a clean, layered backend: controller → service → repository, DTO mapping, bean validation, and database migrations.

## Tech stack

| Concern | Choice |
|---|---|
| Language / runtime | Java 25 |
| Framework | Spring Boot 3.5.16 (Web, Data JPA, Security, Validation) |
| Database | PostgreSQL 16 |
| Migrations | Flyway |
| Mapping | MapStruct |
| API docs | springdoc-openapi (Swagger UI) |
| Testing | JUnit 5, Mockito, AssertJ, Spring MockMvc |
| Build | Maven |
| Containerisation | Docker + Docker Compose |

## API

All endpoints are under `/users` and produce `application/json`.

| Method | Path | Description |
|---|---|---|
| `POST` | `/users` | Create a user |
| `GET` | `/users/id/{id}` | Get a user by id |
| `PUT` | `/users/id/{id}` | Update a user |
| `DELETE` | `/users/id/{id}` | Delete a user |
| `GET` | `/users/firstName?...` | Find by first name |
| `GET` | `/users/lastName?...` | Find by last name |
| `GET` | `/users/rsaId?...` | Find by RSA ID number |
| `GET` | `/users/search?...` | Search users |

Interactive docs once running: **http://localhost:8080/swagger-ui.html**

## Running locally

This API uses the shared portfolio PostgreSQL instance and Docker network, so start those once from the portfolio root first:

```bash
# from the portfolio root — creates the central Postgres + portfolio-net
docker-compose up -d
```

### With Docker Compose (recommended)

```bash
docker compose up --build
```

The API joins `portfolio-net`, connects to the central Postgres, runs its Flyway migrations, and listens on **http://localhost:8080**. Configuration is driven by environment variables (local defaults point at the central DB):

| Variable | Default | Purpose |
|---|---|---|
| `DB_HOST` | `localhost` (`postgres` in compose) | Database host |
| `DB_PORT` | `5432` | Database port |
| `DB_NAME` | `usersdb` | This service's database |
| `DB_USERNAME` | `postgres` | Database user |
| `DB_PASSWORD` | `postgres` | Database password |

### With Maven (needs the central PostgreSQL running)

```bash
mvn spring-boot:run
```

## Testing

```bash
mvn test
```

**29 tests across two layers, no external services required:**

| Layer | Scope |
|---|---|
| Service (`@ExtendWith(MockitoExtension)`) | Business rules against a mocked repository and mapper — add/remove/update, duplicate RSA ID, not-found cases, lookups and search |
| Controller (`@WebMvcTest`) | HTTP contract with a mocked service — `201`/`200`/`204` status codes, bean-validation failures → `400`, non-numeric path id → `400`, and the `@ControllerAdvice` mapping of domain exceptions → `404` |

Both layers are pure unit / web-slice tests — they never touch a database — so `mvn test` is fully self-contained, which is exactly what CI runs on every push.

## Project layout

```
src/main/java/za/co/example
├── UserApplication.java      # entry point
├── controller                # HTTP layer (UsersController)
├── service                   # business logic
├── repository                # Spring Data JPA
├── entity / dto              # persistence + API models
└── mapper                    # MapStruct entity <-> DTO
src/main/resources/db/migration  # Flyway migrations
```

## License

See [LICENSE](LICENSE).
