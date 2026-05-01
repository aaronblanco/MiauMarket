# MiauMarket v2

Aplicacion Android para explorar productos para gatos mediante una API REST. Incluye autenticacion, catalogo con busqueda y navegacion a detalle de producto, con sesion persistida para mantener al usuario identificado entre cierres de la app.

La app esta pensada como una base solida de cliente Android: consume datos del backend, separa la logica en capas y mantiene el acceso a la API centralizado en repositorios. La informacion de sesion se guarda en DataStore y la interfaz se construye con Jetpack Compose.

## Tecnologias principales

- Kotlin + Jetpack Compose
- Retrofit + Moshi
- Hilt (inyeccion de dependencias)
- Jetpack DataStore (sesion)
- Navegacion y estado compartido entre pantallas

## Documentacion

La documentacion del proyecto y de la capa de datos esta en `Docs/`.

