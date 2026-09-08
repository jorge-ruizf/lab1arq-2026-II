# UdeaBank — Lab 1 Arquitectura de Software



## ▶️ Ejecución rápida

```bash
docker compose up --build
```

| Servicio   | URL                          |
|------------|------------------------------|
| Frontend   | http://localhost:4321        |
| Backend    | http://localhost:8088/api    |
| Base de datos | localhost:3307 (MySQL)    |

---

## Stack tecnológico

**Backend** — Java 17 con Spring Boot 4, arquitectura REST. Persistencia con Spring Data JPA + Hibernate sobre MySQL 8. Build con Maven.

**Frontend** — Astro, consumiendo los endpoints REST mediante fetch nativo desde el cliente. Sin frameworks adicionales.

**Base de datos** — MySQL 8 en contenedor Docker con volumen persistente.

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
