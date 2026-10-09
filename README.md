[![CI/CD Pipeline](https://github.com/jorge-ruizf/lab1arq-2026-II/actions/workflows/build.yml/badge.svg)](https://github.com/jorge-ruizf/lab1arq-2026-II/actions/workflows/build.yml)
[![Quality gate status](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=alert_status)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Coverage](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=coverage)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=sqale_rating)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=reliability_rating)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=security_rating)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=ncloc)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Technical Debt](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=sqale_index)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Duplicated Lines (%)](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=duplicated_lines_density)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Maintainability issues](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=software_quality_maintainability_issues)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Reliability issues](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=software_quality_reliability_issues)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)
[![Security issues](https://sonarcloud.io/api/project_badges/measure?project=jorge-ruizf_lab1arq-2026-II&metric=software_quality_security_issues)](https://sonarcloud.io/summary/new_code?id=jorge-ruizf_lab1arq-2026-II)

# UdeaBank — Lab 1 Arquitectura de Software



## ▶️ Ejecución rápida

```bash
docker compose up --build
```

| Servicio   | URL                          |
|------------|------------------------------|
| Frontend   | http://localhost:4321        |
| Backend    | http://localhost:8088/api    |
| Base de datos | localhost:5433 (PostgreSQL 16)  |

---

## Stack tecnológico

**Backend** — Java 17 con Spring Boot 4, arquitectura REST. Persistencia con Spring Data JPA + Hibernate sobre PostgreSQL. Build con Maven.

**Frontend** — Astro, consumiendo los endpoints REST mediante fetch nativo desde el cliente. Sin frameworks adicionales.

**Base de datos** — PostgreSQL 16 en contenedor Docker con volumen persistente.

**Variables de entorno** — ver [ENV.md](ENV.md).

**Infraestructura** — Docker + Docker Compose orquestando los tres servicios (frontend, backend, db) con healthcheck y dependencias entre contenedores.

---

## Endpoints disponibles

| Método | Ruta | Descripción |
|--------|------|-------------|
| GET | `/api/customers` | Lista todos los clientes |
| GET | `/api/customers/{id}` | Cliente por ID |
| POST | `/api/customers` | Crear cliente |
| POST | `/api/transactions` | Transferir dinero entre cuentas |
| GET | `/api/transactions/{accountNumber}` | Transacciones de una cuenta |

&copy; Jorge Andrés Ruiz Flores - 08/Sept/2026 
