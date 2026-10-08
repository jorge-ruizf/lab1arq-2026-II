# Variables de entorno — UdeaBank

Los `.env.example` de cada servicio son la plantilla versionada; los `.env` reales
están en `.gitignore` y **nunca se commitean**.

> **Spring Boot NO lee `.env` por sí solo.** En local hay que exportar las variables
> antes de arrancar (`export SPRING_DATASOURCE_URL=...`) o usar `docker compose`,
> que las inyecta en el servicio `backend`. En Render las variables se configuran
> directamente en la UI. El frontend (Astro) sí lee `.env` automáticamente en build.

---

## backend/ → Render (Service) → Environment

Plantilla: `backend/.env.example`

| Variable | Valor / ejemplo | Descripción |
|---|---|---|
| `PORT` | *(no se define)* | Render lo asigna solo; `server.port=${PORT:8088}`. |
| `SPRING_DATASOURCE_URL` | `jdbc:postgresql://<host-neon>/neondb?sslmode=require` | URL JDBC de Neon (PostgreSQL). |
| `SPRING_DATASOURCE_USERNAME` | `neondb_owner` | Usuario de Neon. |
| `SPRING_DATASOURCE_PASSWORD` | *(solo en Render/`.env` local)* | Contraseña de Neon. Nunca en el repo. |
| `CORS_ALLOWED_ORIGINS` | `https://lab1arq-2026-ii.jorge-ruizf.workers.dev,https://*-lab1arq-2026-ii.jorge-ruizf.workers.dev,http://localhost:4321` | Orígenes permitidos, separados por coma (`allowedOriginPatterns`, admite comodín `*`). |

- **Local:** `set -a; source backend/.env; set +a` y arrancar con `mvn spring-boot:run`,
  o simplemente `docker compose up` (usa la URL `jdbc:postgresql://db:5432/udeabank`).
- **Render:** pega las variables en **Dashboard → tu servicio → Environment**.

## frontend/ → Cloudflare Workers → Variables / Build variables

Plantilla: `frontend/.env.example`

| Variable | Valor local | Valor en Cloudflare |
|---|---|---|
| `PUBLIC_API_URL` | `http://localhost:8088/api` | `https://<tu-servicio>.onrender.com/api` (URL **https** de Render) |

- **Local:** leído automáticamente por Astro desde `frontend/.env`.
- **Cloudflare:** define `PUBLIC_API_URL` en **Settings → Variables**
  (y en *Build variables* si el build se ejecuta en la nube).

## db (docker compose local)

| Variable | Valor |
|---|---|
| `POSTGRES_USER` | `postgres` |
| `POSTGRES_PASSWORD` | `postgres` |
| `POSTGRES_DB` | `udeabank` |

Puerto local `5433` → `5432` del contenedor. Si vienes del volumen viejo de MySQL:
`docker compose down -v` antes de levantar de nuevo.
