# UniBank — Frontend

Sitio estático en [Astro](https://astro.build) para la gestión de clientes, transferencias y transacciones del backend Spring Boot.

## Estructura

```text
src/
├── components/    Componentes reutilizables (Header, Footer, formularios, botones)
├── layouts/       BaseLayout.astro (html/head/header/footer reutilizables)
├── pages/         Una ruta por pantalla (/, /crear-cliente, /transacciones)
├── services/      api.ts: cliente centralizado de la API REST
├── styles/        CSS global
└── config.ts      Constantes del sitio (SITE_TITLE, SITE_DESCRIPTION)
```

## Configuración

Copia `.env.example` a `.env` y ajusta la URL del backend:

```sh
cp .env.example .env
```

| Variable         | Descripción                    | Ejemplo                     |
| ---------------- | ------------------------------ | --------------------------- |
| `PUBLIC_API_URL` | URL base del backend (API REST) | `http://localhost:8088/api` |

En Cloudflare debes definir `PUBLIC_API_URL` en las variables de entorno del proyecto (build).

## Comandos

| Command         | Acción                                        |
| --------------- | --------------------------------------------- |
| `npm install`   | Instala dependencias                          |
| `npm run dev`   | Servidor de desarrollo en `localhost:4321`    |
| `npm run build` | Build de producción en `./dist/`              |
| `npm run preview` | Vista previa del build                      |
