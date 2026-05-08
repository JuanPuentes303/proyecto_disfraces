# Documentación de Endpoints API — Disfraces Rivera

Base URL: `http://localhost:8080`

La API REST usa autenticación **JWT (Bearer Token)**. El token se obtiene en `/api/auth/login` y debe enviarse en el header `Authorization: Bearer <token>` en todos los endpoints protegidos.

---

## Autenticación

### `POST /api/auth/login`

Autentica a un usuario y devuelve un token JWT.

**Acceso:** Público

**Request Body (JSON):**
```json
{
  "correo": "usuario@email.com",
  "password": "contraseña"
}
```

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `correo` | string | ✅ | Correo electrónico válido |
| `password` | string | ✅ | Contraseña del usuario |

**Response `200 OK`:**
```json
{
  "token": "eyJhbGciOiJIUzI1NiJ9...",
  "tipo": "Bearer",
  "correo": "usuario@email.com"
}
```

**Errores:**
- `400 Bad Request` — campos vacíos o correo inválido
- `401 Unauthorized` — credenciales incorrectas

---

## Disfraces (Público)

### `GET /api/disfraces`

Lista todos los disfraces activos con soporte de filtros opcionales.

**Acceso:** Público

**Query Params (todos opcionales):**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `busqueda` | string | Busca por nombre del disfraz |
| `categoriaId` | Long | Filtra por ID de categoría |
| `talla` | string | Filtra por talla (ej: `S`, `M`, `L`, `XL`) |
| `genero` | string | Filtra por género (ej: `Masculino`, `Femenino`, `Unisex`) |
| `precioMin` | decimal | Precio mínimo de alquiler |
| `precioMax` | decimal | Precio máximo de alquiler |

**Ejemplo:** `GET /api/disfraces?talla=M&genero=Unisex&precioMax=50000`

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Disfraz de Pirata",
    "descripcion": "Disfraz completo de pirata del caribe",
    "talla": "M",
    "genero": "Masculino",
    "precioAlquiler": 35000.00,
    "precioCompra": 120000.00,
    "disponibleVenta": true,
    "nombreCategoria": "Aventura",
    "imagenPrincipalUrl": "/uploads/disfraces/pirata.jpg"
  }
]
```

---

### `GET /api/disfraces/{id}`

Obtiene el detalle de un disfraz específico.

**Acceso:** Público

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Long | ID del disfraz |

**Response `200 OK`:**
```json
{
  "id": 1,
  "nombre": "Disfraz de Pirata",
  "descripcion": "Disfraz completo de pirata del caribe",
  "talla": "M",
  "genero": "Masculino",
  "precioAlquiler": 35000.00,
  "precioCompra": 120000.00,
  "disponibleVenta": true,
  "nombreCategoria": "Aventura",
  "imagenPrincipalUrl": "/uploads/disfraces/pirata.jpg"
}
```

**Errores:**
- `404 Not Found` — disfraz no encontrado

---

## Reservas (Usuarios autenticados)

> Todos los endpoints de esta sección requieren el header:
> `Authorization: Bearer <token>`

### `GET /api/reservas/mis-reservas`

Devuelve todas las reservas del usuario autenticado.

**Acceso:** `ROLE_USER` o `ROLE_ADMIN`

**Response `200 OK`:**
```json
[
  {
    "id": 5,
    "nombreDisfraz": "Disfraz de Pirata",
    "imagenDisfrazUrl": "/uploads/disfraces/pirata.jpg",
    "tipo": "ALQUILER",
    "fechaInicio": "2025-10-31",
    "fechaFin": "2025-11-01",
    "estado": "ACTIVA",
    "observaciones": "Necesito entrega a domicilio"
  }
]
```

---

### `POST /api/reservas/disfraz/{disfrazId}`

Crea una nueva reserva para el usuario autenticado.

**Acceso:** `ROLE_USER` o `ROLE_ADMIN`

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `disfrazId` | Long | ID del disfraz a reservar |

**Request Body (JSON):**
```json
{
  "fechaInicio": "2025-10-31",
  "fechaFin": "2025-11-01",
  "tipo": "ALQUILER",
  "observaciones": "Necesito entrega a domicilio"
}
```

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `fechaInicio` | LocalDate (`YYYY-MM-DD`) | ✅ | Fecha de inicio (no puede ser pasada) |
| `fechaFin` | LocalDate (`YYYY-MM-DD`) | ✅ | Fecha de fin (no puede ser pasada) |
| `tipo` | enum | ✅ | `ALQUILER` o `COMPRA` |
| `observaciones` | string | ❌ | Notas adicionales (máx. 500 caracteres) |

**Response `200 OK`:**
```json
{
  "mensaje": "Reserva creada correctamente"
}
```

**Errores:**
- `400 Bad Request` — validación fallida
- `409 Conflict` — disfraz no disponible en las fechas indicadas

---

### `GET /api/reservas/disfraz/{disfrazId}/disponibilidad`

Consulta si un disfraz está disponible en un rango de fechas.

**Acceso:** `ROLE_USER` o `ROLE_ADMIN`

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `disfrazId` | Long | ID del disfraz |

**Query Params:**

| Parámetro | Tipo | Requerido | Descripción |
|-----------|------|-----------|-------------|
| `fechaInicio` | string (`YYYY-MM-DD`) | ✅ | Fecha de inicio |
| `fechaFin` | string (`YYYY-MM-DD`) | ✅ | Fecha de fin |

**Ejemplo:** `GET /api/reservas/disfraz/1/disponibilidad?fechaInicio=2025-10-31&fechaFin=2025-11-01`

**Response `200 OK`:**
```json
{
  "disfrazId": 1,
  "fechaInicio": "2025-10-31",
  "fechaFin": "2025-11-01",
  "disponible": true
}
```

---

## Usuario

### `GET /api/usuario/perfil`

Devuelve el perfil básico del usuario autenticado.

**Acceso:** `ROLE_USER` o `ROLE_ADMIN`

**Response `200 OK`:**
```json
{
  "correo": "usuario@email.com",
  "roles": [
    { "authority": "ROLE_USER" }
  ]
}
```

---

## Admin — Disfraces

> Todos los endpoints `/api/admin/**` requieren `ROLE_ADMIN`.

### `GET /api/admin/disfraces`

Lista todos los disfraces (activos e inactivos) con datos de administración.

**Response `200 OK`:**
```json
[
  {
    "id": 1,
    "nombre": "Disfraz de Pirata",
    "descripcion": "Disfraz completo de pirata del caribe",
    "talla": "M",
    "genero": "Masculino",
    "precioAlquiler": 35000.00,
    "precioCompra": 120000.00,
    "disponibleVenta": true,
    "activo": true,
    "nombreCategoria": "Aventura",
    "imagenPrincipalUrl": "/uploads/disfraces/pirata.jpg"
  }
]
```

---

### `POST /api/admin/disfraces`

Crea un nuevo disfraz. El body debe enviarse como `multipart/form-data`.

**Content-Type:** `multipart/form-data`

| Campo | Tipo | Requerido | Descripción |
|-------|------|-----------|-------------|
| `nombre` | string | ✅ | Nombre del disfraz (2–150 caracteres) |
| `descripcion` | string | ❌ | Descripción (máx. 1000 caracteres) |
| `talla` | string | ✅ | Talla del disfraz |
| `genero` | string | ✅ | Género del disfraz |
| `precioAlquiler` | decimal | ✅ | Precio de alquiler (> 0) |
| `precioCompra` | decimal | ❌ | Precio de compra (> 0) |
| `categoriaId` | Long | ✅ | ID de la categoría |
| `disponibleVenta` | boolean | ❌ | `true` por defecto |
| `imagen` | file | ✅ | Imagen principal del disfraz (máx. 10 MB) |

**Response `200 OK`:**
```json
{
  "mensaje": "Disfraz creado correctamente"
}
```

---

### `PUT /api/admin/disfraces/{id}`

Actualiza un disfraz existente. El body debe enviarse como `multipart/form-data`.

**Content-Type:** `multipart/form-data`

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Long | ID del disfraz a actualizar |

Los campos del body son iguales al `POST`. El campo `imagen` es **opcional**: si no se envía, se conserva la imagen anterior.

**Response `200 OK`:**
```json
{
  "mensaje": "Disfraz actualizado correctamente"
}
```

---

### `POST /api/admin/disfraces/{id}/desactivar`

Desactiva (soft delete) un disfraz. Deja de aparecer en el catálogo público.

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Long | ID del disfraz |

**Response `200 OK`:**
```json
{
  "mensaje": "Disfraz desactivado correctamente"
}
```

---

## Admin — Reservas

### `GET /api/admin/reservas`

Lista todas las reservas del sistema con datos completos del cliente.

**Response `200 OK`:**
```json
[
  {
    "id": 5,
    "nombreCliente": "María López",
    "correoCliente": "maria@email.com",
    "telefonoCliente": "3001234567",
    "nombreDisfraz": "Disfraz de Pirata",
    "imagenDisfrazUrl": "/uploads/disfraces/pirata.jpg",
    "tipo": "ALQUILER",
    "fechaInicio": "2025-10-31",
    "fechaFin": "2025-11-01",
    "estado": "ACTIVA",
    "observaciones": "Necesito entrega a domicilio"
  }
]
```

---

### `POST /api/admin/reservas/{id}/cancelar`

Cancela una reserva activa.

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Long | ID de la reserva |

**Response `200 OK`:**
```json
{
  "mensaje": "Reserva cancelada correctamente"
}
```

---

### `POST /api/admin/reservas/{id}/finalizar`

Marca una reserva como finalizada.

**Path Params:**

| Parámetro | Tipo | Descripción |
|-----------|------|-------------|
| `id` | Long | ID de la reserva |

**Response `200 OK`:**
```json
{
  "mensaje": "Reserva finalizada correctamente"
}
```

---

## Estados y Enumeraciones

### `EstadoReserva`

| Valor | Descripción |
|-------|-------------|
| `ACTIVA` | Reserva vigente |
| `FINALIZADA` | Reserva completada |
| `CANCELADA` | Reserva cancelada |
| `VENCIDA` | Reserva vencida sin finalizar |

### `TipoReserva`

| Valor | Descripción |
|-------|-------------|
| `ALQUILER` | El cliente alquila el disfraz por un periodo |
| `COMPRA` | El cliente compra el disfraz definitivamente |

---

## Resumen de Accesos

| Endpoint | Método | Acceso |
|----------|--------|--------|
| `/api/auth/login` | POST | Público |
| `/api/disfraces` | GET | Público |
| `/api/disfraces/{id}` | GET | Público |
| `/api/reservas/mis-reservas` | GET | Autenticado |
| `/api/reservas/disfraz/{id}` | POST | Autenticado |
| `/api/reservas/disfraz/{id}/disponibilidad` | GET | Autenticado |
| `/api/usuario/perfil` | GET | Autenticado |
| `/api/admin/disfraces` | GET | ADMIN |
| `/api/admin/disfraces` | POST | ADMIN |
| `/api/admin/disfraces/{id}` | PUT | ADMIN |
| `/api/admin/disfraces/{id}/desactivar` | POST | ADMIN |
| `/api/admin/reservas` | GET | ADMIN |
| `/api/admin/reservas/{id}/cancelar` | POST | ADMIN |
| `/api/admin/reservas/{id}/finalizar` | POST | ADMIN |