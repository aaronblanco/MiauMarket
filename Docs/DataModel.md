# Documentacion del Modelo de Datos - MiauMarket

Este documento describe solo los datos que afectan al consumo de la app Android (Retrofit/Moshi + DataStore) contra el backend comun.

## 1. Persistencia local en Android (DataStore)

### Nombre: session_prefs
Representa la sesion activa en el dispositivo.

| Campo | Tipo Kotlin | Obligatorio | Descripcion |
| :--- | :--- | :--- | :--- |
| jwt_token | String | No | JWT para enviar en Authorization: Bearer <token>. |

Ejemplo real:
```json
{
  "jwt_token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxIiwiZW1haWwiOiJwZXBlQGV4YW1wbGUuY29tIiwicm9sZSI6InVzZXIifQ.signature"
}
```

## 2. Contrato API que consume Android

Base path: /api

### 2.1 Auth

#### POST /api/auth/register
Escritura de usuario y apertura de sesion.

Request:
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| firstName | String | Si |
| lastName | String | Si |
| birthDate | String (yyyy-MM-dd) | Si |
| email | String | Si |
| password | String | Si |
| role | String | No (default user) |

Response (201):
```json
{
  "token": "<jwt>",
  "user": {
    "id": 1,
    "firstName": "Pepe",
    "lastName": "Garcia",
    "birthDate": "1998-05-21T00:00:00.000Z",
    "email": "pepe@example.com",
    "role": "user"
  }
}
```

#### POST /api/auth/login
Inicio de sesion.

Request:
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| email | String | Si |
| password | String | Si |

Response (200):
```json
{
  "token": "<jwt>",
  "user": {
    "id": 1,
    "firstName": "Pepe",
    "lastName": "Garcia",
    "birthDate": "1998-05-21T00:00:00.000Z",
    "email": "pepe@example.com",
    "role": "user"
  }
}
```

#### GET /api/auth/me
Lectura del usuario autenticado.

Header recomendado en Android:
Authorization: Bearer <jwt_token>

Response (200):
```json
{
  "user": {
    "id": 1,
    "firstName": "Pepe",
    "lastName": "Garcia",
    "birthDate": "1998-05-21T00:00:00.000Z",
    "email": "pepe@example.com",
    "role": "user"
  }
}
```

#### POST /api/auth/logout
Invalidacion de sesion en cliente (el backend responde confirmacion).

Response (200):
```json
{ "ok": true }
```

### 2.2 Products

#### GET /api/products
Lectura paginada del catalogo.

Query params:
| Parametro | Tipo | Obligatorio | Descripcion |
| :--- | :--- | :--- | :--- |
| search | String | No | Filtro por title o source. |
| take | Int | No | Tamano de pagina (default 20). |
| skip | Int | No | Offset (default 0). |

Response (200):
```json
{
  "total": 128,
  "take": 20,
  "skip": 0,
  "items": [
    {
      "id": 57,
      "source": "kiwoko",
      "title": "Rascador arbol para gatos",
      "price": 45.95,
      "rawPrice": "45.95 EUR",
      "currency": "EUR",
      "url": "https://www.kiwoko.com/gatos/rascador-x",
      "image": "https://cdn.kiwoko.com/rascador-x.jpg",
      "scrapedAt": "2026-04-07T10:32:12.000Z",
      "createdAt": "2026-04-07T10:33:01.000Z",
      "updatedAt": "2026-04-07T10:33:01.000Z"
    }
  ]
}
```

#### GET /api/products/:id
Lectura de detalle por id numerico.

#### POST /api/products
Creacion manual de producto (requiere rol admin).

#### PUT /api/products/:id
Actualizacion de producto (requiere rol admin).

#### DELETE /api/products/:id
Borrado de producto (requiere rol admin).

## 3. DTOs Kotlin recomendados (ajustados al contrato real)

### ProductResponse
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| id | Long | Si |
| source | String | Si |
| title | String | Si |
| price | Double? | No |
| rawPrice | String? | No |
| currency | String | Si |
| url | String | Si |
| image | String? | No |
| scrapedAt | String | Si |
| createdAt | String | Si |
| updatedAt | String | Si |

Nota UI: si la app quiere usar name, se puede mapear localmente title -> name sin cambiar el JSON recibido.

### ProductsListResponse
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| total | Int | Si |
| take | Int | Si |
| skip | Int | Si |
| items | List<ProductResponse> | Si |

### UserResponse
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| id | Long | Si |
| firstName | String | Si |
| lastName | String | Si |
| birthDate | String | Si |
| email | String | Si |
| role | String | Si |

### AuthResponse
| Campo | Tipo Kotlin | Obligatorio |
| :--- | :--- | :--- |
| token | String | Si |
| user | UserResponse | Si |

## 4. Relaciones y cardinalidad relevantes para Android

1. Usuario -> Sesion en dispositivo (1 : 0..1)
Un usuario puede tener como maximo un jwt_token guardado en este dispositivo.

2. Products (lista) -> Product (1 : N)
GET /api/products devuelve una coleccion paginada de productos en items.

3. Producto -> Fuente (N : 1 logica)
Cada producto tiene un source textual (kiwoko, manual) para identificar origen.

## 5. Decisiones de dataset que afectan a la app

- Se conservan id, title, price, currency, image, url y source porque son los campos usados por listado, detalle y filtros.
- Se descartan descripciones largas y metadatos no usados para reducir trafico y complejidad en Android.
- birthDate se captura en UX como dd/MM/aaaa y antes de enviar se transforma a yyyy-MM-dd.
