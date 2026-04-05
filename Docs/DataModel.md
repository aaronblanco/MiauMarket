# Documentación del Modelo de Datos - MiauMarket

Este documento detalla la estructura de datos utilizada en la aplicación MiauMarket, cubriendo tanto la persistencia local como los datos consumidos desde el backend.

## 1. Persistencia Local (DataStore)

La aplicación utiliza **Jetpack DataStore (Preferences)** para el manejo de sesiones de usuario. Al ser un almacén de clave-valor, se documenta la "tabla" lógica de preferencias.

### Nombre: `session_prefs`
Representa el estado de la sesión activa del usuario.

| Campo | Tipo | Obligatorio | Descripción |
| :--- | :--- | :--- | :--- |
| `jwt_token` | String | Opcional | Token de autenticación JWT devuelto por el servidor. |

**Ejemplo de registro:**
```json
{
  "jwt_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
}
```

---

## 2. Modelos de Backend (Retrofit / Moshi)

MiauMarket consume una API REST. A continuación se detallan las estructuras de datos (DTOs) que la app maneja en Kotlin.

### A. Colección: `Products`
Representa el catálogo de productos para gatos scrapeados o creados manualmente.

| Campo | Tipo | Obligatorio | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | String | Sí | Identificador único del producto. |
| `title` | String | Sí | Nombre comercial del producto. |
| `price` | Double | Sí | Precio numérico actual. |
| `currency` | String | Opcional | Moneda (ej. "EUR"). Por defecto "EUR". |
| `image` | String | Opcional | URL de la imagen del producto. |
| `url` | String | Opcional | Enlace a la tienda original (Kiwoko, etc). |
| `source` | String | Opcional | Origen del producto (ej. "kiwoko", "manual"). |

**Ejemplo de registro:**
```json
{
  "id": "prod_8821x",
  "title": "Rascador para gatos Árbol Miau",
  "price": 45.95,
  "currency": "EUR",
  "image": "https://cdn.kiwoko.com/rascador.jpg",
  "url": "https://www.kiwoko.com/p/rascador-arbol",
  "source": "kiwoko"
}
```

### B. Colección: `Users`
Representa a los usuarios registrados en el sistema.

| Campo | Tipo | Obligatorio | Descripción |
| :--- | :--- | :--- | :--- |
| `id` | String | Sí | ID único generado por el backend. |
| `firstName` | String | Sí | Nombre del usuario. |
| `lastName` | String | Sí | Apellido del usuario. |
| `email` | String | Sí | Correo electrónico (usado para login). |
| `birthDate` | String | Sí | Fecha de nacimiento (ISO 8601). |
| `role` | String | Sí | Rol del usuario ("user" o "admin"). |

**Ejemplo de registro:**
```json
{
  "id": "user_001",
  "firstName": "Pepe",
  "lastName": "García",
  "email": "pepe@example.com",
  "birthDate": "1998-05-21",
  "role": "user"
}
```

---

## 3. Relaciones y Cardinalidad

Debido a que la aplicación actual consume datos de forma reactiva desde una API REST, las relaciones se gestionan mediante identificadores en los DTOs:

1.  **Producto -> Fuente (1:1)**: Cada producto tiene un campo `source` que indica su origen. Se implementa como un String en el DTO `ProductResponse`.
2.  **Usuario -> Sesión (1:1)**: Un usuario identificado por su `id` tiene una única sesión activa en el dispositivo, representada por el `jwt_token` en DataStore.
3.  **Carrito de Compra (Próxima Fase - 1:N)**: Un Usuario podrá tener múltiples productos en su carrito. Se implementará referenciando el `id` del producto en una colección `Cart`.

---

## 4. Decisiones sobre el Dataset

Para el catálogo de productos, se ha decidido:
*   **Conservar**: `title`, `price`, `image` y `url`. Son esenciales para la comparación de precios y la redirección a compra.
*   **Descartar**: Descripciones largas y metadatos técnicos del scraping original para optimizar el tráfico de red en el móvil y mantener una interfaz limpia enfocada en la búsqueda rápida.
*   **Transformación**: El campo `rawPrice` del backend se convierte a `Double` en la app para permitir filtros por rango de precio.
